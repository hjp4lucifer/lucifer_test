package cn.lucifer.demo.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.HttpHelper;
import cn.lucifer.http.HttpMethod;

/**
 * http://www.phimvublog.org/
 */
public class PhimvublogDemo {
	public static Logger log = Logger.getLogger("lucifer_test");

	final HashMap<String, String> httpHeads = new HashMap<>();

	private final String baseSavePath = "E:/limit/许诺Sabrina/phimvublog/";

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
		String base_url_format_1 = "https://images-blogger-opensocial.googleusercontent.com/gadgets/proxy?url=http://www.xiuren.org/xiuren/XiuRen-N00481/00%02d.jpg&container=blogger&gadget=a&rewriteMime=image/*";

		for (int i = 1; i <= 40; i++) {
			String url = String.format(base_url_format_1, i);
			log.info(url);
			saveFile(url, String.format("%02d.jpg", i));
		}
	}

	protected void saveFile(String url, String fn) {
		log.info("src=\t" + url);

		String pathname = baseSavePath + fn;
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
	}
}
