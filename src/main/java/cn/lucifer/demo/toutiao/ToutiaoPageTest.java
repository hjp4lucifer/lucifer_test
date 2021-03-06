package cn.lucifer.demo.toutiao;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.HttpHelper;
import cn.lucifer.http.HttpMethod;

/**
 * 今日头条抓取视频
 * 
 * @author Lucifer
 *
 */
public class ToutiaoPageTest {
	public static Logger log = Logger.getLogger("lucifer_test");

	final HashMap<String, String> httpHeads = new HashMap<>();

	final HashMap<Integer, String> videoStates = new HashMap<>();

	@Before
	public void setUp() throws Exception {
		// 获取视频link

		// http://www.toutiao.com/i6360431282081497602/
		// http://www.toutiao.com/a6393429145929777409/
		// http://ib.365yg.com/video/urls/v/1/toutiao/mp4/cc188098177f4c63a73dbf24a1a62108?r=8506826699700343&s=3878875934&callback=tt_playerwqzdl
		// http://v6.365yg.com/video/m/114362400000e3612cb5bc42200264ded77613476fa3c938ce0308283b/?Expires=1488968513&AWSAccessKeyId=qh0h9TdcEMrm1VlR2ad%2F&Signature=jJivBx5kpp2VPjWSGG39TR9wVDo%3D

		httpHeads.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		httpHeads.put("Connection", "Keep-Alive");
		httpHeads.put("Accept-Language", "en,zh-CN;q=0.8,zh;q=0.6");
		httpHeads.put("Accept-Encoding", "gzip, deflate, sdch, br");

		videoStates.put(20, "转码失败");
		videoStates.put(30, "转码进行中");
		videoStates.put(40, "视频id不存在");
		videoStates.put(0, "unknown");
		videoStates.put(1, "上传中");
		videoStates.put(2, "上传失败");
		videoStates.put(3, "等待上传");
		videoStates.put(101, "视频被屏蔽");
		videoStates.put(102, "视频被删除");
		videoStates.put(103, "视频永久删除");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		String url = "http://www.toutiao.com/i6360431282081497602/";
		url = "http://www.toutiao.com/a6393429145929777409/";
		byte[] data = HttpHelper.http(url, HttpMethod.GET, httpHeads, null);
		String html = new String(data);
		log.debug(html);

		String videoid = getVideoId(html);
		// videoid = "97d8f99942e440e78fd97c406d304003";
		// videoid = "e4a9b736110b42d2acf21e68b0de9f1a";

		String videoUrl = getVideoUrl(videoid);
		log.info(videoUrl);
		
		URL url$ = new URL(videoUrl);
		File destination = new File("F:/toutiao/" + StringUtils.replaceChars(url$.getPath(), '/', '_') + ".mp4");
		FileUtils.copyURLToFile(url$, destination);
	}

	public String getVideoId(String html) throws Exception {
		String regex = "<script>.*?</script>";
		Pattern expression = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = expression.matcher(html);

		String str = null;
		while (matcher.find()) {
			if (matcher.group().indexOf("videoid:") > 0) {
				str = matcher.group();
				log.debug(str);
				break;
			}
		}

		if (null == str) {
			throw new Exception("头条改规则了!!!");
		}

		String findStr = "videoid:";
		int beginIndex = str.indexOf(findStr);
		int endIndex = StringUtils.indexOf(str, ",", beginIndex);
		String videoId = str.substring(beginIndex + findStr.length(), endIndex);

		char c = videoId.charAt(0);
		if (c == '\'' || c == '"') {
			videoId = videoId.substring(1);
		}
		c = videoId.charAt(videoId.length() - 1);
		if (c == '\'' || c == '"') {
			videoId = videoId.substring(0, videoId.length() - 1);
		}

		log.info(videoId);

		return videoId;
	}

	public String getVideoUrl(String videoid) throws Exception {
		String apiUrl = getVideoApiUrl(videoid);
		// + "&callback=tt_player" + Integer.toHexString((int) (Math.random() *
		// 100000)); //这里原本想伪装jsonp, 但json解释有些麻烦, 还是算了...
		log.info(apiUrl);

		// http://ib.365yg.com/video/urls/v/1/toutiao/mp4/97d8f99942e440e78fd97c406d304003?r=5759517829188027&s=3612680016
		byte[] data = HttpHelper.http(apiUrl, HttpMethod.GET, httpHeads, null);

		JSONObject json = JSON.parseObject(new String(data));
		if (!new Integer(0).equals(json.getInteger("code"))) {
			throw new HttpClientException("头条 API异常!!!");
		}

		JSONObject jsonData = json.getJSONObject("data");
		if (jsonData.getInteger("status") != 10) {
			throw new HttpClientException(String.format("头条 的视频status=(%d)%s", jsonData.getInteger("status"),
					videoStates.get(jsonData.getInteger("status"))));
		}

		// 查找最高清的视频
		JSONObject video_list = jsonData.getJSONObject("video_list");
		JSONObject highVideo = null;
		int highLv = 0;
		JSONObject tmpVideo;
		for (String video_list_key : video_list.keySet()) {
			tmpVideo = video_list.getJSONObject(video_list_key);
			String definition = tmpVideo.getString("definition");
			// Ex:480p, 360p
			int lv;
			try {
				lv = Integer.parseInt(definition.substring(0, definition.lastIndexOf("p")));
			} catch (NumberFormatException e) {
				throw new Exception("头条 获取视频清晰度的方式变了!!!");
			}
			if (lv > highLv) {
				highVideo = tmpVideo;
				highLv = lv;
			}
		}

		if (null == highVideo) {
			throw new Exception("头条 获取视频video_list的方式变了!!!");
		}

		return new String(Base64.decodeBase64(highVideo.getString("main_url")));
	}

	final String remoteURL = "//ib.365yg.com/video/urls/v/1/toutiao/mp4/";
	final String httpHost = "http://ib.365yg.com";

	public String getVideoApiUrl(String videoid) {
		// TODO 这里奇怪的命名方式, 是根据js里相同的...
		// 源在tt-video.js里的config.remoteURL里
		String t = remoteURL + videoid;
		final String comStr = ".com";
		String r = t.substring(t.indexOf(comStr) + comStr.length()) + "?r="
				+ String.valueOf(Math.random()).substring(2);
		if (!r.startsWith("/")) {
			r = "/" + r;
		}
		log.info(r);
		long i = crc32(r);
		return httpHost + r + "&s=" + i;
	}

	@Test
	public void testDecodeBase64() {
		String code = "aHR0cDovL3Y3LnBzdGF0cC5jb20vNGJjNmFmOTljM2U4MjQ5NjRkZTIxMjU2YjAxMDQ2YmIvNThjNjRlNDkvdmlkZW8vbS8yMjBkMmJlYzkxMWNkMTI0MTc1OWEwNjBlZGQ3ZmEwNDBkYTExNDQwYTEwMDAwMjU5YTA5MWFiYjRmLw==";
		code = "aHR0cDovL3Y2LjM2NXlnLmNvbS92aWRlby9tLzIyMGQyYmVjOTExY2QxMjQxNzU5YTA2MGVkZDdmYTA0MGRhMTE0NDBhMTAwMDAyNTlhMDkxYWJiNGYvP0V4cGlyZXM9MTQ4OTM5NDc3NyZBV1NBY2Nlc3NLZXlJZD1xaDBoOVRkY0VNcm0xVmxSMmFkJTJGJlNpZ25hdHVyZT0zJTJGQUUydzRDUE9ST3VOdldMJTJGWGhJMmxuUkQwJTNE";
		byte[] bytes = Base64.decodeBase64(code);
		log.info(new String(bytes));
	}

	@Test
	public void testCrc32() {
		String t = "/video/urls/v/1/toutiao/mp4/97d8f99942e440e78fd97c406d304003?r=9141224532989212";
		log.info(crc32(t));
	}

	public long crc32(String t) {
		CRC32 crc32 = new CRC32();
		crc32.update(t.getBytes());
		return crc32.getValue();
	}

}
