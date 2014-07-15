package cn.lucifer.demo.jsoup;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import cn.lucifer.util.HttpClientHelper;

/**
 * <a>http://book.sfacg.com/</a>的彩页
 * 
 * @author Lucifer
 * 
 */
public class SfacgImg {

	// private final String basePath = "resource/img";
	private final String site = "http://book.sfacg.com";
	private final String basePath = "E:/book/jp/恶魔高校DxD/img";
	

	@Test
	public void testHighSchoolDxD() throws IOException {
		// final String url = "http://book.sfacg.com/Novel/12225/20943/218977/";
		final String url = "http://book.sfacg.com/Novel/12225/59514/397320";
		final String prefix = "15_";
		final String format = prefix + "%03d_%s";

		int index = 0;
		
		byte[] respone = HttpClientHelper.httpGet(url, null);
		Document doc = Jsoup.parseBodyFragment(new String(respone));
		Elements imgs = doc.select("#ChapterBody img");
		String imgSrc;
		File imgFile;
		byte[] imgData;
		String child;
		for (Element img : imgs) {
			imgSrc = img.attr("src");
			if (!imgSrc.startsWith("http:")) {
				imgSrc = site + imgSrc;
			}
			child = String.format(format, ++index, getFileName(imgSrc));
			System.out.println(imgSrc + "\t\tto\t" + child);
			imgFile = new File(basePath, child);
			imgData = HttpClientHelper.httpGet(imgSrc, null);
			FileUtils.writeByteArrayToFile(imgFile, imgData);
		}
	}

	private String getFileName(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}

}
