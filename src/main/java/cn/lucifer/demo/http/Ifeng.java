package cn.lucifer.demo.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import cn.lucifer.util.HttpClientHelper;


/**
 * 凤凰网汽车
 * 
 * @author Lucifer
 * 
 */
public class Ifeng {
	final String baseUrl = "http://car.auto.ifeng.com/photo/";

	@Test
	public void testGetList() throws IOException {
		String url = "http://car.auto.ifeng.com/photo/1599109";
		String savePath = "D:/pic/2012广州车展/雪铁龙/47";
		File floder = new File(savePath);
		if (!floder.exists()) {
			floder.mkdirs();
		}
		Set<String> urlSet = new HashSet<String>();
		urlSet.add(url);
		parseHtml(url, floder, urlSet);
	}

	protected void parseHtml(String url, File floder, Set<String> urlSet)
			throws IOException {
		byte[] respone = HttpClientHelper.httpGet(url, null);
		Document doc = Jsoup.parse(new String(respone));
		String title = doc.select("#b_img").attr("title");
		System.out.println(title + "\t" + url);
		String c_src = doc.select("div.pic_source a").get(0).attr("href");
		String s_p = floder.getAbsolutePath()
				+ c_src.substring(c_src.lastIndexOf("/" + 1));
		System.out.println("save pic : " + s_p);
		FileOutputStream outputStream = new FileOutputStream(s_p);
		IOUtils.write(HttpClientHelper.httpGet(c_src, null), outputStream);
		IOUtils.closeQuietly(outputStream);
		Elements imgList = doc.select("#img_list img");
		for (Element img : imgList) {
			String id = img.attr("id");
			String imgTitle = img.attr("title");
			if (!title.equals(imgTitle)) {
				System.out.println("Ignore : " + imgTitle);
				continue;
			}
			String newUrl = baseUrl + id.substring(id.indexOf("_") + 1);
			if (urlSet.add(newUrl)) {
				parseHtml(newUrl, floder, urlSet);
			}
		}
	}

}
