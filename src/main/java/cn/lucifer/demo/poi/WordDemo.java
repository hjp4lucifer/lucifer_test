/**
 * 
 */
package cn.lucifer.demo.poi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class WordDemo {

	@Test
	public void testMain() throws IOException {
		String folderPath = "E:/books/诛仙";
		merge(folderPath);
		// String folderPath = "E:/books/云海玉弓缘";
		// mergeOnlyOneFolder(folderPath);
		System.out.println("ok!");
	}

	public void merge(String folderPath) throws IOException {
		File folder = new File(folderPath);
		if (!folder.isDirectory()) {
			throw new RuntimeException("要指定文件夹!");
		}
		File outText = new File(folderPath + System.currentTimeMillis()
				+ ".txt");
		File[] folders = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return true;
				}
				return false;
			}
		});
		Arrays.sort(folders, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		BufferedWriter out = new BufferedWriter(new FileWriter(outText));
		for (File fd : folders) {
			File[] files = fd.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.indexOf(".doc") > 1) {
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
			boolean isFirst = true;
			for (File file : files) {
				if (isFirst) {
					out.append(fd.getName()).append("\r\n\r\n");
					isFirst = false;
				}
				out.append(getText(file)).append("\r\n");
				out.flush();
			}
		}
		out.flush();
		out.close();
	}

	public void mergeOnlyOneFolder(String folderPath) throws IOException {
		File folder = new File(folderPath);
		if (!folder.isDirectory()) {
			throw new RuntimeException("要指定文件夹!");
		}
		File outText = new File(folderPath + System.currentTimeMillis()
				+ ".txt");

		BufferedWriter out = new BufferedWriter(new FileWriter(outText));
		File[] files = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.indexOf(".doc") > 1) {
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
		for (File file : files) {
			out.append(getText(file)).append("\r\n");
			out.flush();
		}
		out.flush();
		out.close();
	}

	public String getText(File docFile) throws IOException {
		FileInputStream in = new FileInputStream(docFile);
		WordExtractor extractor = new WordExtractor(in);
		String txt = extractor.getText();
		extractor.close();
		IOUtils.closeQuietly(in);
		return txt;
	}
}
