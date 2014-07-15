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

/**
 * @author Lucifer
 * 
 */
public class FixNumber {
	static String regexZhang = "[一|二|三|四|五|六|七|八|九|十|百|零|\\d]{1,5}[章|节]";

	public static void main(String[] args) throws IOException {
		String path = "E:/books/徐公子胜治/地师/";
		String fileName = path + "地师.bak.txt";
		String outName = path + System.currentTimeMillis() + ".txt";
		Pattern pattern = Pattern.compile(regexZhang);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), "gbk"));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outName)), "gbk"));
		String line;
		Matcher matcher;
		int index = 0;
		while ((line = reader.readLine()) != null) {
			matcher = pattern.matcher(line);
			boolean not_clean = true, show = true;
			if (matcher.find() && line.trim().length() < 30) {
				String s = matcher.group();
				if (line.indexOf("第") < 0 && line.indexOf("：") < 0&& line.indexOf(":") < 0) {
					line = line.replace(s, "第" + s.substring(0, s.length() - 1)
							+ "章 ");
				} else {
					show = false;
				}
				line = line.replaceAll("　", "").trim();
				if (line.indexOf("部") > 0) {
					not_clean = false;
				} else {
					index++;
					if (show) {
						System.out.println(index + line);
					}
				}
			}
			if (not_clean) {
				writer.append(line).append("\r\n");
			}
		}

		writer.flush();
		writer.close();
		reader.close();
		System.out.println("ok!" + index);
	}

	public static void lingsan(String[] args) throws IOException {
		String path = "E:/books/灵山/";
		String fileName = path + "灵山.txt";
		String outName = path + System.currentTimeMillis() + ".txt";
		Pattern pattern = Pattern.compile("\\d{3}回、");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), "gbk"));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outName)), "gbk"));
		String line;
		Matcher matcher;
		int index = 0;
		while ((line = reader.readLine()) != null) {
			matcher = pattern.matcher(line);
			if (matcher.find() && line.length() < 50) {
				index++;
				String s = matcher.group();
				line = line.replace(s, "第" + s);
				System.out.println(index + line);
			}
			writer.append(line).append("\r\n");
		}

		writer.flush();
		writer.close();
		reader.close();
		System.out.println("ok!" + index);
	}

	public static void renyu(String[] args) throws IOException {
		String path = "E:/books/人欲/";
		String fileName = path + "人欲.txt";
		String outName = path + System.currentTimeMillis() + ".txt";
		Pattern pattern = Pattern.compile("\\d{3}、");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), "gbk"));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outName)), "gbk"));
		String line;
		Matcher matcher;
		int index = 0;
		while ((line = reader.readLine()) != null) {
			matcher = pattern.matcher(line);
			if (matcher.find() && line.length() < 50) {
				index++;
				String s = matcher.group();
				line = line.replace(s, "第" + s.substring(0, s.length() - 1)
						+ "回 ");
				System.out.println(line);
			}
			writer.append(line).append("\r\n");
		}

		writer.flush();
		writer.close();
		reader.close();
		System.out.println("ok!" + index);
	}

	static String[] ads = { "[手机电子书网 http://www.517z.com]",
			"[手机电子书网 www.517z.com]" };

	/**
	 * @param args
	 * @throws IOException
	 * @throws
	 */
	public static void shenyou(String[] args) throws IOException {
		String path = "E:/books/神游/";
		String fileName = path + "神游_lucifer.txt";
		String outName = path + System.currentTimeMillis() + ".txt";
		Pattern pattern = Pattern.compile("\\d{3}回");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), "gbk"));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outName)), "gbk"));
		String line;
		Matcher matcher;
		int index = 0;
		while ((line = reader.readLine()) != null) {
			matcher = pattern.matcher(line);
			if (matcher.find() && line.indexOf("篇") > 0 && line.length() < 50) {
				index++;
				String s = matcher.group();
				line = line.replace(s, "第" + s);
				System.out.println(line);
			}
			if (line.indexOf("517") > 0) {
				line = line.toLowerCase();
				for (String ad : ads) {
					line = line.replace(ad, "");
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
