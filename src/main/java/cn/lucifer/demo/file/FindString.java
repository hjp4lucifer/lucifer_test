package cn.lucifer.demo.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class FindString {

	// static String keyword = "広瀬ゆな";
	String keyword = "みづな";

	// static String keyword = "Claire Castel";// Michelle

	@Test
	public void testMain() throws IOException {
		String folderPath = "D:/limit/doc";
		File folder = new File(folderPath);
		find(folder);
		// System.in.read();
	}

	protected void find(File folder) throws IOException {
		File[] files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				}
				return file.getName().endsWith(".html");
			}
		});

		for (File file : files) {
			if (file.isDirectory()) {
				find(file);
				continue;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "gbk"));
			List<String> lines = IOUtils.readLines(reader);
			IOUtils.closeQuietly(reader);
			String title = null;
			for (String line : lines) {
				if (line.startsWith("<title>")) {
					title = line.substring("<title>".length(),
							line.lastIndexOf("</title>"));
				}
				if (line.indexOf(keyword) > 0) {
					System.out.println(file.getPath() + "\t" + title + "\t\t"
							+ line);
					break;
				}
			}
		}
	}

}
