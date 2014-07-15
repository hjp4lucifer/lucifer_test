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
public class FixTextDemo {

	@Test
	public void testMain(String[] args) throws IOException {
		String path = "E:/books/紫屋魔恋/长篇/";
		String fileName = path + "游龙传.bak.txt", outName = path
				+ System.currentTimeMillis() + ".txt";

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), "gbk"));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outName)), "gbk"));

		String before = null, line;
		Pattern pattern = Pattern.compile("（\\s?[０-９|\\d]{1,3}\\s?）");
		Matcher matcher;
		while ((line = reader.readLine()) != null) {
			if (before != null) {
				matcher = pattern.matcher(before);
				if (matcher.find()) {// is title
					String number = matcher.group();
					int endIndex = number.length() - 1;
					if (before.startsWith("　")) {
						before = before.replaceAll("　", "");
					}
					before = before.replace(
							number,
							String.format("第%s章 ",
									number.substring(1, endIndex)));
					before = before.trim();
					writer.append("\r\n");
					System.out.println(before);
				}

				writer.append(before);
				if (line.length() == 0) {
					writer.append("\r\n");
				}
			}
			before = line;
		}
		writer.append(before);
		writer.flush();
		writer.close();
		reader.close();
		System.out.println("ok!");
	}

	public void yack(String[] args) throws IOException {
		String path = "E:/books/紫屋魔恋/长篇/";
		String fileName = path + "鹰翔长空.bak.txt", outName = path
				+ System.currentTimeMillis() + ".txt";

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), "utf-8"));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outName)), "gbk"));

		String before = null, line;
		Pattern pattern = Pattern.compile("　　鹰翔长空（[０-９]{1,3}）");
		Matcher matcher;
		while ((line = reader.readLine()) != null) {
			if (before != null) {
				matcher = pattern.matcher(before);
				if (matcher.find()) {// is title
					String number = matcher.group();
					int endIndex = number.length() - 1;
					before = before.replace(
							number,
							String.format("第%s章 ",
									number.substring(7, endIndex)));
					before = before.trim();
					writer.append("\r\n");
					System.out.println(before);
				}

				writer.append(before);
				if (line.length() == 0) {
					writer.append("\r\n");
				}
			}
			before = line;
		}
		writer.append(before);
		writer.flush();
		writer.close();
		reader.close();
		System.out.println("ok!");
	}

	public void lqxn(String[] args) throws IOException {
		String path = "E:/books/紫屋魔恋/长篇/";
		String fileName = path + "浪情俠女.bak.txt", outName = path
				+ System.currentTimeMillis() + ".txt";

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), "utf-8"));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outName)), "gbk"));

		String before = null, line;
		Pattern pattern = Pattern.compile("（[０-９]{1,3}）|（２１完）");
		Matcher matcher;
		while ((line = reader.readLine()) != null) {
			if (before != null) {
				matcher = pattern.matcher(before);
				if (matcher.find()) {// is title
					String number = matcher.group();
					int endIndex = number.length() - 1;
					if (before.indexOf('完') > 0) {
						endIndex--;
					}
					before = before.replace(
							number,
							String.format("第%s章 ",
									number.substring(1, endIndex)));
					before = before.trim();
					writer.append("\r\n");
					System.out.println(before);
				}

				writer.append(before);
				if (line.length() == 0) {
					writer.append("\r\n");
				}
			}
			before = line;
		}
		writer.append(before);
		writer.flush();
		writer.close();
		reader.close();
		System.out.println("ok!");
	}

	/**
	 * @param args
	 */
	public void xxmz(String[] args) throws IOException {
		String path = "E:/books/潜龙/";
		String fileName = path + "【仙侠魔踪】(全书完).bak.txt", outName = path
				+ System.currentTimeMillis() + ".txt";

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), "gbk"));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outName)), "gbk"));

		String before = null, line;
		Pattern pattern = Pattern
				.compile("第[一|二|三|四|五|六|七|八|九|十|\\d]{1,3}集[一|二|三|四|五|六|七|八|九|十|\\d]{1,3}回完");
		while ((line = reader.readLine()) != null) {
			if (before != null) {
				if (pattern.matcher(before).find()) {
					before = "\r\n";
				}
				if (before.indexOf('集') > 0 && before.indexOf('/') > 0) {
					// is title
					String[] t = before.split("/");
					before = "";
					for (int i = 0; i < t.length; i++) {
						before += t[i].trim() + " ";
					}
					System.out.println(before);
				}

				writer.append(before);
				if (line.length() == 0) {
					writer.append("\r\n");
				}
			}
			before = line;
		}
		writer.append(before);
		writer.flush();
		writer.close();
		reader.close();
		System.out.println("ok!");
	}

}
