package cn.lucifer.demo.miaopai;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.HttpHelper;
import cn.lucifer.http.HttpMethod;

public class VideoPageTest {
	final HashMap<String, String> httpHeads = new HashMap<>();

	@Before
	public void setUp() throws Exception {
		httpHeads.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		httpHeads.put("Connection", "Keep-Alive");
		httpHeads.put("Accept-Language", "en,zh-CN;q=0.8,zh;q=0.6");
		httpHeads.put("Accept-Encoding", "gzip, deflate, sdch, br");
	}

	@After
	public void tearDown() throws Exception {
	}

	public static Logger log = Logger.getLogger("lucifer_test");

	@Test
	public void test() throws Exception {
		String url = "http://www.miaopai.com/show/Pawr5S-OAuyWzVByGelGDZ2BOGk_.htm";

		byte[] data = HttpHelper.http(url, HttpMethod.GET, httpHeads, null);
		String html = new String(data);
		log.debug(html);

		String video_url = getVideoUrl(html);
		log.info(video_url);

		File folder = new File("F:/miaopai");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		URL url$ = new URL(video_url);
		String fn = StringUtils.replaceChars(url$.getPath(), '/', '_');
		File f = new File(folder, fn);

		FileOutputStream outputStream = new FileOutputStream(f);
		for (int i = 0; i < 10; i++) {
			try {
				HttpHelper.http(video_url, HttpMethod.GET, httpHeads, null, 3600000, null, outputStream, 15000);
				log.info("download = " + video_url + " finished !!!!");
				httpHeads.remove("RANGE");
				break;
			} catch (IOException e) {
				httpHeads.put("RANGE", "bytes=" + f.length() + "-");
				log.error(String.format("download fail(%d), file length=%d, url=%s ", i, f.length(), video_url));
			}
		}
	}

	@Test
	public void testTestGetVideoUrl() throws Exception {
		File file = new File("resource/miaopai.html");
		String html = FileUtils.readFileToString(file, "utf-8");
		log.debug(html);

		String url = getVideoUrl(html);
		log.info(url);
	}

	protected String getVideoUrl(String html) throws Exception {
		String regex = "<script>.*?</script>";
		Pattern expression = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = expression.matcher(html);

		String str = null;
		while (matcher.find()) {
			if (matcher.group().indexOf("MiaopaiPlayer(") > 0) {
				str = matcher.group();
				log.debug(str);
				break;
			}
		}

		if (null == str) {
			throw new Exception("秒拍改规则了!!!");
		}

		String findStr = "videoSrc";
		int beginIndex = str.indexOf(findStr);
		int endIndex = StringUtils.indexOf(str, "\",", beginIndex);
		String url = str.substring(beginIndex + findStr.length(), endIndex);
		if (!url.startsWith("http")) {
			url = url.substring(url.indexOf("http"));
		}
		log.info(url);

		return url;
	}

}
