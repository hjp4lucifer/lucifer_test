package cn.lucifer.demo.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import cn.lucifer.util.HttpClientHelper;

/**
 * 白猫计划
 * 
 * @author Lucifer
 *
 */
public class ShironekoHelper {

	@Test
	public void testGetHelpCode() throws MalformedURLException {
		String uri = "http://shironekoproject.gamerch.com/%E8%A1%80%E5%88%80%E3%83%8E%E9%81%93%E9%80%A3%E3%83%AC%20%E5%8D%94%E5%8A%9B%E5%8B%9F%E9%9B%86%E6%8E%B2%E7%A4%BA%E6%9D%BF";
		byte[] rep = HttpClientHelper.httpGet(uri, null);
		String html = new String(rep);
		Document doc = Jsoup.parse(html);
		Elements elements = doc
				.select("#js_comment_area .ui_posting_text .ui_child_comment_font");
		for (Element element : elements) {
			System.out.println(element.text());
		}
	}

	@Test
	public void testRuningGetHelper() throws MalformedURLException {
		String uri = "http://shironekoproject.gamerch.com/%E8%A1%80%E5%88%80%E3%83%8E%E9%81%93%E9%80%A3%E3%83%AC%20%E5%8D%94%E5%8A%9B%E5%8B%9F%E9%9B%86%E6%8E%B2%E7%A4%BA%E6%9D%BF";
		HashSet<String> hash = new HashSet<>(50);
		while (true) {
			byte[] rep = HttpClientHelper.httpGet(uri, null);
			String html = new String(rep);
			Document doc = Jsoup.parse(html);
			Elements elements = doc
					.select("#js_comment_area .ui_posting_text .ui_child_comment_font");
			for (Element element : elements) {
				String txt = element.text();
				boolean notIn = hash.add(txt);
				if (notIn) {
					System.out.println(txt);
				}
				if (hash.size() > 48) {
					hash.clear();
					System.out.println("---------------");
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
