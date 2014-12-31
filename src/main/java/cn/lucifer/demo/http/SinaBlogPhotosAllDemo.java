package cn.lucifer.demo.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import cn.lucifer.util.HttpClientHelper;

public class SinaBlogPhotosAllDemo {

	@Test
	public void testMain() throws IOException {
		String baseSaveFloderPath = "D:/pic/model/柳岩/photos";
		String listUrl = "http://photo.blog.sina.com.cn/categorys/p/1259871184";
		File f_base = new File(baseSaveFloderPath);
		if (!f_base.exists()) {
			f_base.mkdirs();
		}

		File[] files = f_base.listFiles();
		List<String> simpleFolderNames = new ArrayList<String>();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					simpleFolderNames.add(f.getName());
				}
			}
		}

		List<String> pageUrls = new ArrayList<String>();
		pageUrls.add(listUrl);
		List<String> isReadUrls = new ArrayList<String>();
		isReadUrls.add(listUrl + "/page1");

		Document doc = null;
		do {
			String url = SinaBlogPhotosDemo.getNextListUrl(pageUrls,
					isReadUrls, doc);
			if (url == null) {
				break;
			}
			isReadUrls.add(url);

			String html = new String(HttpClientHelper.httpGet(url, null));
			doc = Jsoup.parse(html);
			Elements photos = doc.select("ul.pt_list_nb div.pt_border_bg a");
			for (Element photo : photos) {
				String link = photo.attr("href");
				if (link.startsWith("http://photo.blog.sina.com.cn/blogpiclist/u/")) {
					System.err.println("博文配图: \t" + link);
					continue;
				}
				processPhoto(photo.attr("href"), baseSaveFloderPath,
						simpleFolderNames);
			}

		} while (true);
	}
	
	public void processPhoto(String listUrl, String baseSaveFloderPath,
			List<String> simpleFolderNames) throws IOException {
		System.out.println("load url : \t" + listUrl);
		String baseSavePath = null;

		List<String> pageUrls = new ArrayList<String>();
		pageUrls.add(listUrl);
		List<String> isReadUrls = new ArrayList<String>();
		isReadUrls.add(listUrl + "/page1");

		Document doc = null;
		do {
			String url = SinaBlogPhotosDemo.getNextListUrl(pageUrls,
					isReadUrls, doc);
			if (url == null) {
				break;
			}
			isReadUrls.add(url);

			String html = new String(HttpClientHelper.httpGet(url, null));
			doc = Jsoup.parse(html);

			if (baseSavePath == null) {// 第一次读取
				String name = doc.select("span#ding_title").get(0).text();
				name = SinaBlogDemo.fixFolderPath(name);
				Elements nums = doc
						.select("div.photobody div.pt_link span.pt_num");
				String time = nums.get(0).text().replaceAll("\\(|\\)|\\-", "");
				String photoCount = nums.get(1).text();
				String folderName = time + " " + name + photoCount;
				System.out.println(folderName);
				for (String sfn : simpleFolderNames) {
					if (folderName.equals(sfn)) {
						throw new RuntimeException(folderName + "\t已经存在！");
					}
				}
				baseSavePath = baseSaveFloderPath + "/" + folderName;
				File f = new File(baseSavePath);
				if (!f.exists()) {
					f.mkdirs();
				}
			}

			Elements aTags = doc.select("ul.pt_list_nb div.pt_border a");
			String imagePageUrl;
			for (Element imgA : aTags) {
				imagePageUrl = imgA.attr("href");
				SinaBlogPhotosDemo.parseImagePageAndSaveSource(imagePageUrl,
						baseSavePath);
			}
		} while (true);

	}

}
