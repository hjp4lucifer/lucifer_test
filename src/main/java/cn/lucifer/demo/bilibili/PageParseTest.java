package cn.lucifer.demo.bilibili;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

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
		app_key = "f3bb208b3d081dc8";
		log.info("app_key=" + app_key);
		secret_key = "1c15888dc316e05a15fdd0a02ed6584f";
		log.info("secret_key=" + secret_key);
	}

	private String app_key;
	private String secret_key;

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void test() throws Exception {
		String url = "http://interface.bilibili.com/playurl?sign=399564451373d1ef1273185cc53e8550&cid=14788672&from=miniplay&player=1";
		log.info(url);
		byte[] data = HttpHelper.http(url, HttpMethod.GET, httpHeads, null);
		String xml = new String(data);
		log.debug(xml);
		
		Video video = XStreamUtils.readVideo(xml);
		System.out.println(video.durl.backup_url);
		System.out.println(JSON.toJSONString(video));
	}

	@Test
	public void testParseUrl() throws IOException, HttpClientException {
		String url = "https://www.bilibili.com/video/av8982798/?tg";
		log.info(url);

		byte[] data = HttpHelper.http(url, HttpMethod.GET, httpHeads, null);
		String html = new String(data);
		log.debug(html);
	}

	protected String getLink(long cid) {
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
