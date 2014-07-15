/**
 * 
 */
package cn.lucifer.demo.jsoup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Lucifer
 * 
 */
public class Test {

	public static void main(String[] args) throws IOException {
		String basePath = "E:/books/资治通鉴/content";
		// File file = new File(basePath + "/zztj_001.htm");
		
		// String resource = read(file);

		File outFile = new File(basePath + System.currentTimeMillis() + ".txt");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "gbk"));

		File folder = new File(basePath);
		File[] files = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.indexOf(".htm") > 1) {
					return true;
				}
				return false;
			}
		});

		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (int i = 0; i < files.length; i++) {
			save(files[i], writer);
		}
		writer.flush();
		writer.close();
		System.out.println("ok!");

		// IOUtils.copy(
		// new InputStreamReader(new FileInputStream(outFile), "utf-8"),
		// new OutputStreamWriter(new FileOutputStream(outFile
		// .getAbsolutePath() + "b.txt"), "gbk"));
	}

	protected static void save(File file, BufferedWriter writer)
			throws IOException {
		Document doc = Jsoup.parse(file, "gbk");
		Elements titles = doc.select("font[face$=楷体_GB2312]");
		for (Element title : titles) {
			String strTitle = title.text();
			strTitle = strTitle.substring(0, 1) + strTitle.substring(2) + '卷';
			System.out.println(strTitle);
			writer.write(strTitle);
			writer.write("\r\n");
		}

		Elements contents = doc.getElementsByTag("p");
		for (Element content : contents) {
			if (content.hasAttr("align")) {
				continue;
			}
			Elements res = content.select("font[color$=#FFFAF5]");
			for (Element re : res) {
				re.remove();
			}
			// System.out.println(content.text());
			writer.write(content.text().replaceAll(" ", " "));
			writer.write("\r\n");
		}
		writer.flush();
	}

	static String read(File file) throws IOException {
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "gbk"));

		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}
}
