package cn.lucifer.demo.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import cn.lucifer.util.HttpClientHelper;

public class SinaBlogPhotosDemo {

	/**
	 * 相册专用
	 * 
	 * @param args
	 * @throws IOException
	 */
	@Test
	public void testMain() throws IOException {
		String baseSavePath = "D:/pic/model/柳岩/photos/20120103 柳岩 芒果画报封面女郎(8)";
		String listUrl = "http://photo.blog.sina.com.cn/category/u/1259871184/s/408657";
		File f = new File(baseSavePath);
		if (!f.exists()) {
			f.mkdirs();
		}

		List<String> pageUrls = new ArrayList<String>();
		pageUrls.add(listUrl);
		List<String> isReadUrls = new ArrayList<String>();
		isReadUrls.add(listUrl + "/page1");

		Document doc = null;
		do {
			String url = getNextListUrl(pageUrls, isReadUrls, doc);
			if (url == null) {
				break;
			}
			isReadUrls.add(url);

			String html = new String(HttpClientHelper.httpGet(url, null));
			doc = Jsoup.parse(html);
			Elements aTags = doc.select("ul.pt_list_nb div.pt_border a");
			String imagePageUrl;
			for (Element imgA : aTags) {
				imagePageUrl = imgA.attr("href");
				parseImagePageAndSaveSource(imagePageUrl, baseSavePath);
			}
		} while (true);

	}

	@Test
	public void testParseImagePageAndSaveSource(String[] args)
			throws IOException {
		String imagePageUrl = "http://photo.blog.sina.com.cn/photo/4aea0959t87cd47696020";
		parseImagePageAndSaveSource(imagePageUrl, null);
	}

	public static void parseImagePageAndSaveSource(String imagePageUrl,
			String baseSavePath) throws IOException {
		System.out.println(imagePageUrl
				+ "\t is current process image page url !");
		String html = new String(HttpClientHelper.httpGet(imagePageUrl, null));
		Document doc = Jsoup.parse(html);
		Elements as = doc.select("div.photoContDesc div.addons a");
		for (Element a : as) {
			if ("原图".equals(a.text())) {
				saveFile(a.attr("href"), baseSavePath);
				// System.out.println(a.attr("href"));
				break;
			}
		}
	}

	public static int image_count = 0;
	public static int retry_count = 0;

	public static void saveFile(String url, String baseSavePath)
			throws IOException {
		String pathname = baseSavePath + url.substring(url.lastIndexOf("/"))
				+ ".jpg";
		// if (true) {
		byte[] data = HttpClientHelper.httpGet(url, null);
		if (data.length == 10063) {// 新浪博客默认图片大小(找不到图片时)
			System.err.println(++retry_count + "\tdefault image :\t" + url);
			if (retry_count < 5) {
				saveFile(url, baseSavePath);
				return;
			}
		}
		retry_count = 0;
		OutputStream output = new FileOutputStream(new File(pathname));
		IOUtils.write(data, output);
		IOUtils.closeQuietly(output);
		// }
		System.out.println(++image_count + "\tsave finish : " + pathname);
	}

	public static String getNextListUrl(List<String> pageUrls,
			List<String> isReadUrls, Document preDoc) {
		String resutlUrl = null;
		boolean notExists;
		for (String url : pageUrls) {
			notExists = true;
			for (String readUrl : isReadUrls) {
				if (url.equals(readUrl)) {
					notExists = false;
					break;
				}
			}
			if (notExists) {
				resutlUrl = url;
				break;
			}
		}
		if (resutlUrl == null && preDoc != null) {
			Elements as = preDoc.select("ul.SG_pages a");
			String r_url;
			for (Element a : as) {
				r_url = a.attr("href");
				notExists = true;
				for (String url : pageUrls) {
					if (url.equals(r_url)) {
						notExists = false;
						break;
					}
				}
				if (notExists) {
					pageUrls.add(r_url);
				}
			}
			return getNextListUrl(pageUrls, isReadUrls, null);
		}

		return resutlUrl;
	}
}
