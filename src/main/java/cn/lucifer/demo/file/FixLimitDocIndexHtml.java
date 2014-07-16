package cn.lucifer.demo.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * 针对limit doc的index.html进行修正
 * 
 * @author Lucifer
 * 
 */
public class FixLimitDocIndexHtml {

	private String path = "D:/limit/doc/2012全年經典總匯/index.htm";

	final String objectStart = "<OBJECT type=\"text/sitemap\">".toLowerCase();
	final String objectEnd = "</OBJECT>".toLowerCase();
	final String nameParam = "<param name=\"Name\"".toLowerCase();
	final String localParam = "<param name=\"Local\"".toLowerCase();

	@Test
	public void testMain() throws IOException {
		File srcFile = new File(path);
		InputStream input = new FileInputStream(srcFile);

		File newFile = new File(srcFile.getParentFile(), srcFile.getName()
				+ "_bak.htm");
		List<String> lines = IOUtils.readLines(input, "gbk");

		IOUtils.closeQuietly(input);

		List<String> newLines = new ArrayList<String>(lines.size() / 3);

		String name = null, local;
		int index;
		for (String line : lines) {
			line = line.toLowerCase();
			index = line.indexOf(objectStart);
			if (index > -1) {
				newLines.add(line.replace(objectStart, ""));
				continue;
			}
			index = line.indexOf(objectEnd);
			if (index > -1) {
				newLines.add("</li>");
				continue;
			}

			// 对Name参数的处理
			if (line.indexOf(nameParam) > -1) {
				name = getValue(line);
				continue;
			}
			// 对Local参数的处理
			if (line.indexOf(localParam) > -1) {
				local = getValue(line);
				newLines.add(String.format("<a target='_black' href='%s'>%s</a>", local, name));
				name = null;
				continue;
			}
			newLines.add(line);
		}

		FileOutputStream output = new FileOutputStream(newFile);
		IOUtils.writeLines(newLines, null, output, "gbk");
		IOUtils.closeQuietly(output);
	}

	Pattern regexValue = Pattern.compile("(^| )value=\"([^\"]*)(\"|$)");

	public String getValue(String paramLine) {
		Matcher matcher = regexValue.matcher(paramLine);
		if (matcher.find()) {
			return matcher.group(2);
		}
		return null;
	}
}
