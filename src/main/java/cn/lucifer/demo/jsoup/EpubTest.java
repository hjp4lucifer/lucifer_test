package cn.lucifer.demo.jsoup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import cn.lucifer.demo.file.FindAndSortFile;

public class EpubTest {
	protected final String folderPath = "E:/book/默默猴/妖刀记（1-36卷）/OPS";

	@Test
	public void testMain() throws IOException {
		FindAndSortFile tools = new FindAndSortFile();
		File[] files = tools.findBy(folderPath, ".html");

		File outFile = new File(folderPath + System.currentTimeMillis()
				+ ".txt");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "gbk"));

		for (File file : files) {
			System.out.println(file.getAbsolutePath());

			Document doc = Jsoup.parse(file, "utf-8");
			Elements titles = doc.select("h3");
			writeByElements(writer, titles);
			writer.append('\n');

			Elements contents = doc.select("p");
			writeByElements(writer, contents);
			writer.append("\n\n");
		}

		IOUtils.closeQuietly(writer);
	}

	public void writeByElements(BufferedWriter writer, Elements elements)
			throws IOException {
		String txt;
		for (Element element : elements) {
			txt = StringUtils.trimToNull(element.text());
			if (null == txt) {
				continue;
			}
			writer.append(element.text());
			writer.append('\n');
		}
	}

}
