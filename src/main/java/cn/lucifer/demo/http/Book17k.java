package cn.lucifer.demo.http;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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

public class Book17k {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	protected final String baseUrl = "http://www.17k.com";

	protected final String bookIndexPageUri = baseUrl + "/list/1470.html";
	protected final String filePath = "results/桐宫之囚.txt";
	protected final String firstLine = "桐宫之囚\n作者：阿菩";

	private int timeoutMillis = 300000;

	@Test
	public void test() throws IOException {
		parseBookIndexPage();
	}

	protected void parseBookIndexPage() throws IOException {
		File file = new File(filePath);
		System.out.println(file.getAbsolutePath());
		//file.deleteOnExit();

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "gbk"));

		URL url = new URL(bookIndexPageUri);
		Document doc = Jsoup.parse(url, timeoutMillis);
		Elements elements = doc.select(".directory_con div");

		String juan = null, title, href;
		for (Element element : elements) {
			if (element.hasClass("tit")) {
				juan = element.text();
				System.out.println("juan=\t" + juan);
				writer.write(juan);
				writer.write("\n\n");
			} else if (element.hasClass("con")) {
				Elements links = element.select("a");
				for (Element link : links) {
					// http://www.17k.com/chapter/1470/136810.html
					title = link.text();
					href = link.attr("href");
					System.out.println("title=\t" + title + "\turl=" + href);

					IOUtils.write(juan, writer);
					writer.write(' ');
					writer.write(title);
					writer.write('\n');

					parseBookChapterPage(href, writer);
				}
			}
		}

		IOUtils.closeQuietly(writer);
	}

	protected void parseBookChapterPage(String urlStr, BufferedWriter writer)
			throws IOException {
		URL url = new URL(baseUrl + urlStr);
		Document doc = Jsoup.parse(url, timeoutMillis);
		Element element = doc.getElementById("chapterContentWapper");
		String html = element.html();
		html = StringUtils.replace(html, "<br /><br />", "\n");
		writer.write(html);
		writer.write("\n\n");
		writer.flush();
	}
}
