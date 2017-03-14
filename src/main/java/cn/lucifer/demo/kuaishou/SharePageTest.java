package cn.lucifer.demo.kuaishou;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.lucifer.http.HttpHelper;
import cn.lucifer.http.HttpMethod;

/**
 * 快手
 * 
 * @author Lucifer
 *
 */
public class SharePageTest {
	public static Logger log = Logger.getLogger("lucifer_test");

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

	@Test
	public void test() throws Exception {
		String url = "http://www.kuaishou.com/photo/364878822/1740807930";

		byte[] data = HttpHelper.http(url, HttpMethod.GET, httpHeads, null);
		String html = new String(data);
		log.debug(html);

		String videoUrl = getVideoURL(html);
		log.info(videoUrl);
	}

	@Test
	public void testGetVideoURL() throws Exception {
		File file = new File("resource/kuaishou.html");
		String html = FileUtils.readFileToString(file, "utf-8");
		log.debug(html);

		String videoUrl = getVideoURL(html);
		log.info(videoUrl);
	}

	public String getVideoURL(String html) throws Exception {
		String regex = "<video[^>]+>";
		Pattern expression = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = expression.matcher(html);

		String str = null;
		while (matcher.find()) {
			str = matcher.group();
			log.info(str);
			break;
		}

		String findStr = "src=\"";
		int beginIndex = str.indexOf(findStr);
		int endIndex = StringUtils.indexOf(str, "\"", beginIndex + findStr.length());

		String videoUrl = str.substring(beginIndex + findStr.length(), endIndex);
		return videoUrl;
	}

}
