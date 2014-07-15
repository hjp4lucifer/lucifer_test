package cn.lucifer.demo.txt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.junit.Test;

public class TextDemo {

	@Test
	public void testMain() throws IOException {
		String path = "E:/books/随波逐流之一代军师/temp/";
		String sourcePath = path + "随波逐流一代军师_qidian.txt";
		File sourceFile = new File(sourcePath);
		File folder = new File(path + System.currentTimeMillis());
		folder.mkdir();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(sourceFile), "gbk"));
		String line = null;
		File outFile = new File(folder.getAbsolutePath() + "/000前言.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "gbk"));
		int bb = 0;
		while ((line = in.readLine()) != null) {
			if (line.indexOf("章") > 1) {
				line.replaceAll("\t", " ");
				String cc = line.substring(1, line.indexOf("章"));
				if (cc.equals("一")) {
					bb++;
				}
				String num = changeNumber(cc, bb);
				if (num != null) {
					out.flush();
					out.close();
					outFile = new File(folder.getAbsolutePath() + "/" + num
							+ line.substring(line.indexOf("章") + 1).trim()
							+ ".txt");
					out = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(outFile), "gbk"));
				}
			}
			out.append(line + "\r\n");
		}
		out.flush();
		out.close();
		System.out.println("ok!");
	}

	private String changeNumber(String cc, int bb) {
		String num = null;
		for (int i = 0; i < cc.length(); i++) {
			char nn = cc.charAt(i);
			if (nn == '一') {
				num = processNum(num, 1);
			} else if (nn == '二') {
				num = processNum(num, 2);
			} else if (nn == '三') {
				num = processNum(num, 3);
			} else if (nn == '四') {
				num = processNum(num, 4);
			} else if (nn == '五') {
				num = processNum(num, 5);
			} else if (nn == '六') {
				num = processNum(num, 6);
			} else if (nn == '七') {
				num = processNum(num, 7);
			} else if (nn == '八') {
				num = processNum(num, 8);
			} else if (nn == '九') {
				num = processNum(num, 9);
			} else if (nn == '十') {
				num = processNum(num, 10);
			} else {
				num = null;
				break;
			}
		}
		if (num != null) {
			if (num.length() == 1) {
				num = "0" + num;
			}
			num = bb + num;
		}
		return num;
	}

	private String processNum(String num, int nn) {
		if (num == null) {
			num = String.valueOf(nn);
		} else if (num.charAt(num.length() - 1) == '0') {
			if (num.length() > 2
					&& num.substring(num.length() - 2, num.length()).equals(
							"10")) {
				num = num.substring(0, num.length() - 2) + String.valueOf(nn);
			} else {
				num = num.substring(0, num.length() - 1) + String.valueOf(nn);
			}
		} else {
			if (nn == 10) {
				num = num + "0";
			} else {
				num += String.valueOf(nn);
			}
		}
		return num;
	}
}
