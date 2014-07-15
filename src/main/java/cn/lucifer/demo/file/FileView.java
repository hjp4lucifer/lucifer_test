package cn.lucifer.demo.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class FileView {

	@Test
	public void viewFileHead() throws IOException {
		String filename = "G:/eMuleTemp/鮎川直美(鮎川なお)(Nao Ayukawa) 悪戯 鮎川なお 通常版（2008年6月6日、アウダースジャパン）.avi.emule.td";
		String filename2 = "G:/Income/鮎川なお(Nao Ayukawa) - 【Psd-308】恥ずかしいコト。Rebirth (2008.01.01).avi.part";
		File file = new File(filename), file2 = new File(filename2);
		System.out.println(file.length());
		System.out.println(file2.length());
		System.out.println(file.length() - file2.length());

		long minLenght = file2.length();

		FileInputStream input = new FileInputStream(file);
		FileInputStream input2 = new FileInputStream(file2);

		long skip = 0;
		int len = 2048;
		byte[] bytes = new byte[len];
		byte[] bytes2 = new byte[len];

		for (int i = 0; skip * i < minLenght; i++) {
			input.skip(skip * i);
			input.read(bytes);

			input2.skip(skip * i);
			input2.read(bytes2);

			for (int j = 0; j < bytes.length; j++) {
				if (bytes[j] != bytes2[j]) {
					System.out.println(skip * i);
					System.out.println(new String(bytes));
					System.out
							.println("---------------------------------------------------------------------"  + j);
					System.out.println(new String(bytes2));
					return;
				}
			}
		}

		System.out.println(new String(bytes));

		IOUtils.closeQuietly(input);
	}

	// @Test
	public void testURL() throws UnsupportedEncodingException {
		String url = "ed2k://|file|%E9%AE%8E%E5%B7%9D%E7%9B%B4%E7%BE%8E(%E9%AE%8E%E5%B7%9D%E3%81%AA%E3%81%8A)(Nao%20Ayukawa)%20%E6%82%AA%E6%88%AF%20%E9%AE%8E%E5%B7%9D%E3%81%AA%E3%81%8A%20%E9%80%9A%E5%B8%B8%E7%89%88%EF%BC%882008%E5%B9%B46%E6%9C%886%E6%97%A5%E3%80%81%E3%82%A2%E3%82%A6%E3%83%80%E3%83%BC%E3%82%B9%E3%82%B8%E3%83%A3%E3%83%91%E3%83%B3%EF%BC%89.avi|1829524651|A17FDF0EBDD1A2ADF5DC256515C92533|h=ORFLDZDZ2A67Y6LMPWMQ5POTEOWZE3EE|/";
		System.out.println(URLDecoder.decode(url, "utf-8"));
	}

	// @Test
	public void testFileView() throws IOException {
		String fileName = "G:/eMuleTemp/鮎川なお(Nao Ayukawa) - 【Psd-308】恥ずかしいコト。Rebirth (2008.01.01).avi.part.met";
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), "gbk"));
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}
}
