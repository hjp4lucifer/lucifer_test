package cn.lucifer.demo.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class FindString implements IFind {

	// String keyword = "GeoCursorBase";
	String keyword = "目前可领取的手游礼包列表";

	// final String folderPath = "D:/workspace/yinba_web/public/bg/jslib";
	// final String folderPath =
	// "D:/tools/mongodb-src-r2.4.11/mongodb-src-r2.4.11/src";
	// final String folderPath = "F:/lcf/cartoon";
	// final String folderPath =
	// "E:/dev_tools/apache/spark/spark-2.0.2-bin-hadoop2.7/examples/src/main/java/org/apache/spark/examples";
	final String folderPath = "D:/workspace/5253";

	final String fileSuffix = ".java";

	@Test
	public void main() throws IOException {
		File folder = new File(folderPath);
		find(folder, this);
		// System.in.read();
	}

	@Override
	public void find(File file, List<String> lines) throws IOException {
		String title = null;
		for (String line : lines) {
			if (line.startsWith("<title>")) {
				// System.out.println(file.getPath());
				// System.out.println(line);
				title = line.substring("<title>".length(), line.lastIndexOf("</title>"));
			}
			if (line.indexOf(keyword) > -1) {
				System.out.println(file.getPath() + "\t" + title + "\t\t" + line);
				break;
			}
		}
	}

	@Test
	public void clearAd() throws IOException {
		final String adStr = "<script language=javascript src=\"ad1.js\"></script>";
		File folder = new File(folderPath);
		keyword = adStr;
		find(folder, this);
	}

	@Test
	public void findImg() throws IOException {
		FindImg iFindImpl = new FindImg();
		find(new File(folderPath), iFindImpl);
		iFindImpl.showMap();
	}

	private class FindImg implements IFind {
		String regex = "<img[^>]+(^| )src=\"(http(s)?://[^/]*)(/|$)";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		HashMap<String, Integer> map = new HashMap<String, Integer>(100);

		@Override
		public void find(File file, List<String> lines) throws IOException {
			for (String line : lines) {
				checkImgSrc(line);
			}
		}

		void checkImgSrc(String line) {
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				processSite(matcher.group(2));
			}
		}

		void processSite(String url) {
			Integer count = map.get(url);
			if (count == null) {
				map.put(url, 1);
			} else {
				map.put(url, count + 1);
			}
		}

		void showMap() {
			for (Entry<String, Integer> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
			System.out.println("total : " + map.size());
		}
	}

	protected void find(File folder, IFind iFind) throws IOException {
		File[] files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				}
				return file.getName().endsWith(fileSuffix);
			}
		});

		for (File file : files) {
			if (file.isDirectory()) {
				find(file, iFind);
				continue;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			List<String> lines = IOUtils.readLines(reader);
			IOUtils.closeQuietly(reader);

			iFind.find(file, lines);
		}
	}

}
