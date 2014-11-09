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


public class SinaBlogDemo {

	@Test
	public void testMain() throws IOException {
		// String baseSaveFloderPath = "D:/pic/model/佟丽娅(丫丫) F/blog";
		// String baseSaveFloderPath = "D:/pic/model/江伊涵 F/blog";
		// String baseSaveFloderPath = "D:/pic/model/杨棋涵 F/blog";
		// String baseSaveFloderPath = "D:/pic/model/艾尚真/blog";
		// String baseSaveFloderPath = "D:/pic/model/张婉悠/blog";
		// String baseSaveFloderPath = "D:/pic/model/韩一菲/blog";
		// String baseSaveFloderPath = "D:/pic/model/周蕊/blog";
		// String baseSaveFloderPath = "D:/pic/model/叶梓萱/blog";
//		String baseSaveFloderPath = "D:/pic/model/周韦彤/blog";
		String baseSaveFloderPath = "E:/pic/丁琳/blog";
		String listUrl = "http://photo.blog.sina.com.cn/blogpiclist/u/1493272467";
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
			Elements photobody = doc.select("div.photobody");
			Elements pt_title_sub = photobody.select("div.pt_title_sub");
			Elements pt_list_nb = photobody.select("ul.pt_list_nb");

			boolean hasExistsBlog = false;
			for (int i = 0; i < pt_title_sub.size(); i++) {
				Element title = pt_title_sub.get(i);
				String name = title.select("a").get(0).text();
				name = fixFolderPath(name);
				System.out.println(name);
				String des = QQDownloadDemo
						.showMathcer(regex, title.select("em").get(0).text())
						.get(0).replaceAll("\\(|\\)|\\-| |:", "");
				String folderName = String.format("%s %s", des, name);

				boolean folderExists = false;
				for (String n : simpleFolderNames) {
					if (folderName.equals(n)) {
						folderExists = true;
						break;
					}
				}
				if (folderExists) {
					hasExistsBlog = true;
					System.out.println("folder exists : \t" + folderName);
					continue;
				}
				String baseSavePath = baseSaveFloderPath + "/" + folderName;
				new File(baseSavePath).mkdirs();

				Elements photos = pt_list_nb.get(i).select("a");
				for (Element photo : photos) {
					parseImagePageAndSaveSource(photo.attr("href"),
							baseSavePath);
					// System.out.println(photo.attr("href"));
				}
			}

			if (hasExistsBlog) {
				break;
			}
		} while (true);
	}

	public void parseImagePageAndSaveSource(String imagePageUrl,
			String baseSavePath) throws IOException {
		System.out.println(imagePageUrl
				+ "\t is tcurrent process image page url !");
		String html = new String(HttpClientHelper.httpGet(imagePageUrl, null));
		Document doc = Jsoup.parse(html);
		Elements as = doc.select("div.photoContDesc div a");
		for (Element a : as) {
			if ("点击查看原图".equals(a.text())) {
				SinaBlogPhotosDemo.saveFile(a.attr("href"), baseSavePath);
				// System.out.println(a.attr("href"));
				break;
			}
		}
	}

	public static String fixFolderPath(String folderPath) {
		folderPath = folderPath.replaceAll(" ", " ");
		folderPath = folderPath.replaceAll("•", "·");
		folderPath = folderPath.replaceAll(":|\\*|\\?|\"|<|>|\\||\\.", "");
		return folderPath;
	}

	@Test
	public void testReplaceAll() {
		String name = "20120322083.8 奇|妙<见>面会:选*美?皇\"后";
		name = name.replaceAll(":|\\*|\\?|\"|<|>|\\||\\.", "");
		System.out.println(name);
	}

	private final String regex = "\\([\\d| |\\-|:]+\\)";

	@Test
	public void testShowMatcher() {
		String source = "共4张图片(2012-01-22 10:59)";
		String s = QQDownloadDemo.showMathcer(regex, source).get(0)
				.replaceAll("\\(|\\)|\\-| |:", "");

		System.out.println(s);
		System.out.println((int) ' ');
	}

	@Test
	public void testDownload() throws IOException {
		String url = "http://s16.sinaimg.cn/orignal/674423bbgb50c534d2cef&690";
		String baseSaveFloderPath = "D:/pic/model/杨棋涵 F/blog";
		SinaBlogPhotosDemo.saveFile(url, baseSaveFloderPath);
	}

	@Test
	public void testDefaultOrignal() {
		byte[] data = HttpClientHelper.httpGet(
				"http://s8.sinaimg.cn/image/default_orignal.gif#4", null);
		System.out.println(data.length);

		File file = new File("resource/default_orignal.gif");
		System.out.println(file.length());
		System.out.println(file.getName());
	}

}
