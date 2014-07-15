package cn.lucifer.demo.jsoup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class ParseHtml {

	@Test
	public void parseIndex() throws IOException {
		String result = "results/ssj.txt";
		String indexFileName = "D:/book/网络玄幻小说合集/01/ssj/index.htm";
		File indexFile = new File(indexFileName);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(result)), "gbk"));

		String parentFilePath = indexFile.getParentFile().getAbsolutePath();
		String html = readAll(indexFileName, "gbk");
		Document doc = Jsoup.parse(html);
		String txt;

		Elements titles = doc.select("tr td font b");
		List<String> titleArray = new ArrayList<String>();
		for (Element title : titles) {
			txt = title.text();
			if (txt.startsWith("第") || txt.startsWith("外传")) {
				titleArray.add(txt);
			}
		}

		int titleIndex = -1;
		Elements a_tags = doc.select("a");
		for (Element a_tag : a_tags) {
			txt = a_tag.text();
			if (txt.startsWith("第一") || txt.startsWith("外传")) {
				titleIndex++;
			} else if (!txt.startsWith("第") && !txt.startsWith("尾") && !txt.startsWith("后")) {
				continue;
			}
			
			readDetail(writer, parentFilePath + "/" + a_tag.attr("href"),
					titleArray.get(titleIndex));
		}

		writer.flush();
		IOUtils.closeQuietly(writer);
	}

	@Test
	public void parseIndexForV29() throws IOException {
		String result = "results/mhj.txt";
		String indexFileName = "D:/book/网络玄幻小说合集/29/mhj/index.htm";
		File indexFile = new File(indexFileName);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(result)), "gbk"));

		String parentFilePath = indexFile.getParentFile().getAbsolutePath();
		String html = readAll(indexFileName, "gbk");
		Document doc = Jsoup.parse(html);
		String txt;

		Elements titles = doc.select("div.content a");
		for (Element title : titles) {
			readDetail(writer, parentFilePath + "/" + title.attr("href"), null);
		}

		writer.flush();
		IOUtils.closeQuietly(writer);
	}
	
	@Test
	public void parseIndexForV31() throws IOException {
		String result = "results/wlqfp.txt";
		String indexFileName = "D:/book/网络玄幻小说合集/31/wlqfp/index.htm";
		File indexFile = new File(indexFileName);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(result)), "gbk"));

		String parentFilePath = indexFile.getParentFile().getAbsolutePath();
		String html = readAll(indexFileName, "gbk");
		Document doc = Jsoup.parse(html);
		String txt;

		Elements titles = doc.select("div.content a");
		for (Element title : titles) {
			readDetail(writer, parentFilePath + "/" + title.attr("href"), null);
		}

		writer.flush();
		IOUtils.closeQuietly(writer);
	}

	protected void readDetail(BufferedWriter writer, String path, String title)
			throws IOException {
		String html = readAll(path, "gbk");
		String txt = null;
		Document doc = Jsoup.parse(html);
		Elements subTitle = doc.select("b font");
		String t = null;
		if (title != null) {
			t = title + " " + subTitle.get(0).text();
		} else {
			t = subTitle.get(0).text();
		}
		System.out.println(t);
		Elements content = doc.select("td p");
		for (Element element : content) {
			if (element.attr("style").equals("line-height: 150%")) {
				txt = element.text();
				if (txt.length() > 100) {
					String[] array = element.html().split("<br />");
					StringBuffer buffer = new StringBuffer();
					for (String s : array) {
						s = s.replaceAll("<.*?>", "").trim();
						if (s.length() != 0) {
							buffer.append(s).append("\r\n");
						}
					}
					txt = buffer.toString();
					// if (true) {
					// System.out.println(txt);
					// throw new NullPointerException();
					// }
					break;
				}
			}
		}
		if (txt == null) {
			throw new NullPointerException();
		}
		writer.append(t).append("\r\n").append(txt).append("\r\n\r\n");
	}

	protected String readAll(String path, String charsetName)
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(path)), charsetName));
		List<String> lines = IOUtils.readLines(reader);
		IOUtils.closeQuietly(reader);

		StringBuffer buffer = new StringBuffer();
		for (String line : lines) {
			buffer.append(line);
		}
		return buffer.toString();
	}
}
