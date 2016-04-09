package cn.lucifer.demo.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.HttpHelper;
import cn.lucifer.http.HttpMethod;

public class BaiduJsonDemo {

	public static Logger log = Logger.getLogger("lucifer_test");

	final HashMap<String, String> httpHeads = new HashMap<>();

	private final String baseSavePath = "D:/pic/baidu/";

	@Before
	public void setUp() throws Exception {
		httpHeads
				.put("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
		httpHeads.put("Connection", "Keep-Alive");
		httpHeads.put("Accept-Language", "en,zh-CN;q=0.8,zh;q=0.6");
		// httpHeads.put("Host", "pic.qqtn.com");
		// httpHeads.put("Accept-Encoding", "gzip, deflate, sdch");
		httpHeads.put("Accept-Encoding", "deflate, sdch");
		httpHeads
				.put("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String base_url_format_1 = "http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=%E5%A4%B4%E5%83%8F&cg=head&pn=";
		String base_url_format_2 = "&rn=100&itg=0&z=0&fr=&lm=-1&ic=0&s=0&st=-1&gsm=1000096";

		for (int i = 0; i < 30; i++) {
			String url = base_url_format_1 + (i * 100) + base_url_format_2;
			log.info(url);
			parseJson(url);
		}
	}

	private void parseJson(String url) {
		try {
			byte[] data = HttpHelper.http(url, HttpMethod.GET, httpHeads, null);
			httpHeads.put("Referer", url);
			JSONObject jsObj = JSON.parseObject(new String(data));
			JSONArray imgs = jsObj.getJSONArray("imgs");
			for (Object imgObj : imgs) {
				JSONObject img = (JSONObject) imgObj;
				String imgURL = img.getString("middleURL");
				saveFile(imgURL);
			}
			httpHeads.remove("Referer");
		} catch (IOException | HttpClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void saveFile(String url) {
		log.info("src=\t" + url);

		String pathname = baseSavePath + UUID.nameUUIDFromBytes(url.getBytes())
				+ getSuffix(url);
		File file = new File(pathname);
		if (file.exists()) {
			return;
		}
		try {
			byte[] data = HttpHelper.http(url, HttpMethod.GET, httpHeads, null);

			OutputStream output = new FileOutputStream(file);
			IOUtils.write(data, output);
			IOUtils.closeQuietly(output);
			log.info("save finish : " + pathname);
		} catch (IOException | HttpClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			Thread.sleep((long) (Math.random() * 2000));
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	protected String getSuffix(String fn) {
		if (null == fn) {
			return ".jpg";
		}
		int lastIndex = fn.lastIndexOf('.');
		if (lastIndex > 0) {
			return fn.substring(lastIndex);
		}
		return ".jpg";
	}
}
