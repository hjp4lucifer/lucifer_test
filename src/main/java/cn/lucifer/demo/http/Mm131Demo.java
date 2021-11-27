package cn.lucifer.demo.http;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.HttpHelper;
import cn.lucifer.http.HttpMethod;
import cn.lucifer.util.HttpClientHelper;

public class Mm131Demo {
	public static Logger log = Logger.getLogger("lucifer_test");

	final HashMap<String, String> httpHeads = new HashMap<>();

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

	private final String base_url = "http://www.mm131.com/";
	private final String index_url = "http://www.mm131.com/";

	private final String baseSavePath = "D:/pic/mm131/";

	private final String img_base_url = "";

	private final HashMap<String, Object> pageMap = new HashMap<>(20);
	private final String pageCssQuery = ".page-en a";
	private final String detailViewCssQuery = null;
	private final String imgCssQuery = ".content-pic img";

	@Test
	public void test() throws IOException {
		byte[] respone = HttpClientHelper
				.httpGet("http://www.mm131.com/", null);
		Document doc = Jsoup.parse(new String(respone));
		Elements aArray = doc.select(".main a");
		for (Element a : aArray) {
			String href = a.attr("href");
			log.info("href=\t" + href);
			if (pageMap.containsKey(href)) {
				continue;
			}
			pageMap.put(href, null);
		}
	}

	protected String parseListView(String url) throws IOException {
		Document doc = parseToDetailView(url);
		Elements pageArray = doc.select(pageCssQuery);
		for (Element a : pageArray) {
			String href = a.attr("href");
			log.debug("href=\t" + base_url + href);
			if (pageMap.containsKey(href)) {
				continue;
			}
			pageMap.put(href, null);
			return base_url + href;
		}
		return null;
	}

	protected Document parseToDetailView(String url) throws IOException {
		byte[] respone = HttpClientHelper.httpGet(url, null);
		Document doc = Jsoup.parse(new String(respone));
		Elements aArray = doc.select(detailViewCssQuery);

		for (Element a : aArray) {
			String href = a.attr("href");
			String a_url = base_url + href;
			log.info("href=\t" + a_url);
			parsePhotoListView(a_url);
		}
		return doc;
	}

	protected void parsePhotoListView(String url) throws IOException {
		byte[] respone = HttpClientHelper.httpGet(url, null);
		Document doc = Jsoup.parse(new String(respone));
		Elements imgArray = doc.select(imgCssQuery);
		for (Element img : imgArray) {
			String src = img.attr("src");
			src = StringUtils.removeStart(src, img_base_url);

			saveFile(src);
		}
	}

	protected void saveFile(String src) {
		String imgUrl = img_base_url + StringUtils.removeStart(src, "\\");
		log.info("src=\t" + src);

		String pathname = StringUtils.replaceChars(src, "/", "_");
		File file = new File(baseSavePath + pathname);
		if (file.exists()) {
			return;
		}
		try {
			byte[] data = HttpHelper.http(imgUrl, HttpMethod.GET, httpHeads,
					null);

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
