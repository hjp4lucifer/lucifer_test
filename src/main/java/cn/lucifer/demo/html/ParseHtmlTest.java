/**
 * 
 */
package cn.lucifer.demo.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @author Lucifer
 * 
 */
public class ParseHtmlTest {

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException,
			IOException {
		String pathname = "E:/books/史/新唐书/001.htm";
		File xmlFile = new File(pathname);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
				xmlFile), "GB2312");
//		reader.re
	}

}
