/**
 * 
 */
package cn.lucifer.demo.txt;

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

import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class ExtractDemo {

	@Test
	public void testMain() throws IOException {
		String path = "E:/books/潜龙/ql/08/txt";
		File folder = new File(path), outFile = new File(path + "/"
				+ System.currentTimeMillis() + ".txt");

		File[] files = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf(".txt") > 0 && name.length() < 17) {
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

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "gbk"));
		for (int i = 0; i < files.length; i++) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(files[i]), "gbk"));

			String line;
			while ((line = reader.readLine()) != null) {
				if (line.indexOf(title_sign) > 0) {// is title
				// String buffer = "";
				// boolean titleStart = false;
				// for (int j = line.length() - 1; j > -1; j--) {
				// char c = line.charAt(j);
				// if (c == ' ' || c == '，' || c == '。' || c == '（'
				// || c == '）' || c == '(' || c == ')' || c == '【'
				// || c == '】' || '\u4E00' <= c && c <= '\u9FA5') {
				// buffer = c + buffer;
				// titleStart = true;
				// } else if (titleStart) {
				// break;
				// }
				// }
				// System.out.println(buffer);
				// writer.append(buffer + "\r\n\r\n");
					String source = line;
					source = source
							.replace(
									"document.write(\" <p align=center style='FONT-SIZE:13.5pt;font-family:宋体'><b>",
									"");
					source = source.replace("</b><p>\");", "");
					source = source.replaceAll(
							"<a name=\\'\\d{1,2}_\\d{1,2}\\'>", "");
					source = source.replaceAll("<img src=../txt/\\d{1,2}.jpg>", "");
					source = source.replaceAll("<font size=2><font color=red>", "");
					source = source.replaceAll("</font>", "");
					if (source.indexOf("<BR>") > 0) {
						source = source.replaceAll("<BR>", "\r\n");
					}
					System.out.println(source);
					writer.append(source + "\r\n\r\n");
				} else if (line.length() > 1) {// is content
					line = line.replaceAll("<p>", "\r\n");
					if (line.length() > 19) {
						line = line.substring(17, line.length() - 2);
						writer.append(line + "\r\n\r\n");
					}
				}
			}
			writer.flush();

			reader.close();
		}
		writer.flush();
		writer.close();
		System.out.println("ok!");
	}

	final String title_sign = "<p align=center style='FONT-SIZE:13.5pt;font-family:宋体'><b>";
}
