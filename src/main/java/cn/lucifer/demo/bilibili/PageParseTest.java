package cn.lucifer.demo.bilibili;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.HttpHelper;
import cn.lucifer.http.HttpMethod;
import cn.lucifer.util.DigestUtils;

public class PageParseTest {
	public static Logger log = Logger.getLogger("lucifer_test");

	final HashMap<String, String> httpHeads = new HashMap<>();

	@Before
	public void setUp() throws Exception {

		httpHeads.put("Referer", "https://www.bilibili.com/");
		httpHeads.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		httpHeads.put("Connection", "Keep-Alive");
		httpHeads.put("Accept-Language", "en,zh-CN;q=0.8,zh;q=0.6");
		httpHeads.put("Accept-Encoding", "gzip, deflate, sdch, br");

		// String Appkey = "NmY5MGE1OWFjNThhNDEyMw==";
		// String Secretkey = "MGJmZDg0Y2MzOTQwMDM1MTczZjM1ZTY3Nzc1MDgzMjY=";

		// app_key = new String(Base64.decodeBase64(Appkey));
		// app_key = "f3bb208b3d081dc8";
		// log.info("app_key=" + app_key);
		secret_key = "1c15888dc316e05a15fdd0a02ed6584f";
		// secret_key = "edc2c3de6843b26ad6e05206160e8398";
		log.info("secret_key=" + secret_key);

		// 不确定的获取secret_key的方法
		// http://www.bilibili.com/plus/widget/ajaxGetCaptchaKey.php?js&_=1488867096437
		// window.captcha_key = "edc2c3de6843b26ad6e05206160e8398";
	}

	// private String app_key;
	private String secret_key;

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void test() throws Exception {
		// String playurl =
		// "http://interface.bilibili.com/playurl?sign=399564451373d1ef1273185cc53e8550&cid=14788672&from=miniplay&player=1";
		// Video video = getVideo(playurl);
		// System.out.println(JSON.toJSON(video));

		// long cid = 14380320;
		// String url = getLink(cid);
		// System.out.println(url);

		String url;
		url = "https://www.bilibili.com/video/av8982798/?tg";
//		url = "http://bangumi.bilibili.com/anime/5849/play#101637";
		String savePath = "F:/bilibili";
		String subFolderName;
		if (url.indexOf("video") > 0) {
			String[] tmp = StringUtils.split(url, '/');
			subFolderName = savePath + "/video/" + tmp[tmp.length - 2];
		} else if (url.indexOf("anime") > 0) {
			String episode_id = url.substring(url.lastIndexOf("#") + 1);
			subFolderName = savePath + "/anime/" + episode_id;
		} else {
			throw new Exception("b站当前仅支持video和anime!");
		}

		log.info(subFolderName);

		// download
		File folder = new File(subFolderName);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		long cid = getCid(url);
		Video video = getVideo(getPlayUrl(cid));
		for (Durl durl : video.durl) {
			URL url$ = new URL(durl.url);
			log.info("download = " + durl.url + " start !!!!");
			String fn = durl.order + StringUtils.replaceChars(url$.getPath(), '/', '_');
			File f = new File(folder, fn);
			if (f.exists()) {
				log.warn("file=" + f.getAbsolutePath() + " is exists !!!!! ----------");
				continue;
			}
			FileOutputStream outputStream = new FileOutputStream(f);
			for (int i = 0; i < 10; i++) {
				try {
					HttpHelper.http(durl.url, HttpMethod.GET, httpHeads, null, 3600000, null, outputStream, 15000);
					log.info("download = " + durl.url + " finished !!!!");
					httpHeads.remove("RANGE");
					break;
				} catch (IOException e) {
					httpHeads.put("RANGE", "bytes=" + f.length() + "-");
					log.error(String.format("download fail(%d), file length=%d, url=%s ", i, f.length(), durl.url));
				}
			}
		}
	}

	@Test
	public void testParseUrl() throws Exception {
		String url = "https://www.bilibili.com/video/av8982798/?tg";
		url = "http://bangumi.bilibili.com/anime/5849/play#101637";
		log.info(url);

		long cid = 0;
		if (url.indexOf("video") > 0) {
			cid = getCidByVideo(url);
		} else if (url.indexOf("anime") > 0) {
			String episode_id = url.substring(url.lastIndexOf("#") + 1);
			cid = getCidByAnime(episode_id);
		} else {
			throw new Exception("b站当前仅支持video和anime!");
		}
		log.info("cid=" + cid);
	}

	public long getCid(String url) throws Exception {
		log.info(url);

		long cid = 0;
		if (url.indexOf("video") > 0) {
			cid = getCidByVideo(url);
		} else if (url.indexOf("anime") > 0) {
			String episode_id = url.substring(url.lastIndexOf("#") + 1);
			cid = getCidByAnime(episode_id);
		} else {
			throw new Exception("b站当前仅支持video和anime!");
		}
		log.info("cid=" + cid);
		return cid;
	}

	protected long getCidByAnime(String episode_id) throws Exception {
		// episode_id=101637
		log.info("episode_id=" + episode_id);
		InputStream input = IOUtils.toInputStream("episode_id=" + episode_id);
		byte[] data = HttpHelper.http("http://bangumi.bilibili.com/web_api/get_source", HttpMethod.POST, httpHeads,
				input);
		String json = new String(data);
		log.info(json);
		GetSource gs = JSON.parseObject(json, GetSource.class);
		if (gs.code != 0) {
			throw new Exception("b站get_source获取失败!");
		}

		return gs.result.cid;
	}

	protected long getCidByVideo(String url) throws Exception {
		byte[] data = HttpHelper.http(url, HttpMethod.GET, httpHeads, null);
		String html = new String(data);
		log.debug(html);

		String regex = "<script type=('|\")text/javascript('|\")>.*?</script>";
		Pattern expression = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = expression.matcher(html);

		String str = null;
		while (matcher.find()) {
			if (matcher.group().indexOf("EmbedPlayer(") > 0) {
				str = matcher.group();
			}
		}

		if (null == str) {
			throw new Exception("b站获取cid方式更改了!");
		}
		log.info(str);

		String findStr = "cid=";
		int beginIndex = str.indexOf(findStr);
		int endIndex = StringUtils.indexOf(str, '&', beginIndex);
		String cidStr = str.substring(beginIndex + findStr.length(), endIndex);
		log.info(cidStr);

		long cid = Long.parseLong(cidStr);

		return cid;
	}

	protected Video getVideo(String playurl) throws IOException, HttpClientException {
		log.info(playurl);
		byte[] data = HttpHelper.http(playurl, HttpMethod.GET, httpHeads, null);
		String xml = new String(data);
		log.debug(xml);

		Video video = XStreamUtils.readVideo(xml);
		return video;
	}

	protected String getPlayUrl(long cid) {
		// EmbedPlayer('player', "//static.hdslb.com/play.swf",
		// "cid=14788672&aid=8958102&pre_ad=0")
		String subLink = new StrBuilder().append("cid=").append(cid).append("&from=miniplay&player=1").toString();
		String sign = hash(subLink + secret_key);
		log.debug(sign);

		String url = new StrBuilder("http://interface.bilibili.com/playurl?").append("sign=").append(sign).append("&")
				.append(subLink).toString();
		log.info(url);
		return url;
	}

	private String hash(String str) {
		return DigestUtils.md5DigestAsHex(str.getBytes());
	}
}
