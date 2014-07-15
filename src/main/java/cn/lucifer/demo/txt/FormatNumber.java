/**
 * 
 */
package cn.lucifer.demo.txt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class FormatNumber {

	@Test
	public void main() throws IOException {
		String regex = "[０-９]{1,3}";
		Pattern pattern = Pattern.compile(regex);
		String path = "E:/books/紫屋魔恋/长篇/";
		String fileName = path + "梦回天阙.bak.txt";
		String outName = path + System.currentTimeMillis() + ".txt";
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), "gbk"));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outName)), "gbk"));
		String line;
		Matcher matcher;
		while ((line = reader.readLine()) != null) {
			matcher = pattern.matcher(line);
			if (matcher.find() && matcher.start() == 0) {// is title
				String number = matcher.group();
				line = line.replace(number, String.format("第%s章 ", number));
				writer.append("\r\n");
				System.out.println(line);
			}

			writer.append(line).append("\r\n");
		}

		writer.flush();
		writer.close();
		reader.close();
		System.out.println("ok!");
	}

	/**
	 * @param args
	 */
	public void yrasl(String[] args) throws IOException {
		String regexJi = "异人傲世录第[一|二|三|四|五|六|七|八|九|十|\\d]{1,3}集";
		String regexZhang = "第[一|二|三|四|五|六|七|八|九|十|\\d]{1,3}[章|节]";

		String path = "E:/books/异人傲世录/";
		String fileName = path + "异人傲世录.txt";
		String outName = path + System.currentTimeMillis() + ".txt";
		Pattern patternJi = Pattern.compile(regexJi), patternZhang = Pattern
				.compile(regexZhang);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), "unicode"));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outName)), "gbk"));
		String line;
		Matcher matcher;
		int index = 0;
		String indexString = "";
		while ((line = reader.readLine()) != null) {
			matcher = patternJi.matcher(line);
			if (matcher.find()) {
				indexString = matcher.group().substring(5);
				System.out.println("================ " + indexString
						+ " ================ ");
			} else {
				matcher = patternZhang.matcher(line);
				if (matcher.find() && line.length() < 20) {
					line = indexString + " " + line.trim();
					System.out.println(line);
				}
			}

			writer.append(line).append("\r\n");
		}

		writer.flush();
		writer.close();
		reader.close();
		System.out.println("ok!" + index);
	}
}
