package cn.lucifer.demo.txt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 针对CHM解压后，生成的txt文件，是以document.write方式输出
 * 
 * @author Lucifer
 * 
 */
public class DocWriteExtract {

	private static final String book_name = "美人图";
	private static final String resource_folder_parent = "D:/book/limit/河图/"
			+ book_name;
	private static final String resource_folder = resource_folder_parent
			+ "/txt";
	// private static final String result = "results/" + book_name + ".txt";
	private static final String result = resource_folder_parent + "/"
			+ book_name + ".txt";
	private static final String encoding = "gbk";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File outFile = new File(result);
		if (outFile.exists()) {
			outFile.delete();
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "gbk"));

		File[] txtFiles = getTxtFileNames();

		InputStream input;
		List<String> lines;
		for (int i = 0; i < txtFiles.length; i++) {
			input = new FileInputStream(txtFiles[i]);
			lines = IOUtils.readLines(input, encoding);

			writer.append(praseText(lines));

			IOUtils.closeQuietly(input);
			input = null;
		}
		IOUtils.closeQuietly(writer);

	}

	// private static final Regex

	private static final String document_prefix = "document.write";
	private static final int document_prefix_length = document_prefix.length();
	private static final Pattern p_tag_regex_1 = Pattern.compile("<p[^>]+>");
	private static final Pattern p_tag_regex_2 = Pattern.compile("<p>");
	private static final Pattern a_tag_regex = Pattern.compile("<a[^>]+>.*a>");
	private static final Pattern a_tag_ill_regex = Pattern.compile("<a[^>]+>");
	private static final Pattern b_tag_1_regex = Pattern.compile("<b>");
	private static final Pattern b_tag_2_regex = Pattern.compile("</b>");
	private static final Pattern font_tag_1_regex = Pattern
			.compile("<font[^>]+>");
	private static final Pattern font_tag_2_regex = Pattern.compile("</font>");
	private static final Pattern center_tag_regex = Pattern
			.compile("<center>.*center>");
	private static final Pattern span_4_chinese_regex = Pattern.compile("　");

	protected static String praseText(List<String> lines) {
		StringBuffer text = new StringBuffer();
		String[] subLines;
		for (String line : lines) {
			line = StringUtils.trimToNull(line);
			if (line == null) {
				continue;
			}
			line = line.substring(document_prefix_length);// 去掉document_prefix
			line = line.trim();
			line = line.substring(2, line.lastIndexOf(")") - 1);
			line = p_tag_regex_1.matcher(line).replaceAll("");
			line = p_tag_regex_2.matcher(line).replaceAll("\n");
			line = a_tag_regex.matcher(line).replaceAll("");
			line = a_tag_ill_regex.matcher(line).replaceAll("");
			line = b_tag_1_regex.matcher(line).replaceAll("");
			line = b_tag_2_regex.matcher(line).replaceAll("");

			line = font_tag_1_regex.matcher(line).replaceAll("");
			line = font_tag_2_regex.matcher(line).replaceAll("");

			line = center_tag_regex.matcher(line).replaceAll("");
			line = span_4_chinese_regex.matcher(line).replaceAll("");

			subLines = line.split("\n");
			for (String subLine : subLines) {
				subLine = StringUtils.trimToNull(subLine);
				if (subLine != null) {
					text.append(subLine).append("\r\n");
				}
			}
		}
		text.append("\r\n");

		return text.toString();
	}

	protected static File[] getTxtFileNames() {
		File folder = new File(resource_folder);
		File[] txtFiles = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf(".txt") > 0) {
					return true;
				}
				return false;
			}
		});

		Arrays.sort(txtFiles, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				// TODO Auto-generated method stub
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		return txtFiles;
	}

}
