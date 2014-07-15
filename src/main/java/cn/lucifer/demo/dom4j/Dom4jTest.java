/**
 * 
 */
package cn.lucifer.demo.dom4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

/**
 * (太严格了)
 * 
 * @author Lucifer
 * 
 */
public class Dom4jTest {

	/**
	 * 
	 * @param args
	 * @throws DocumentException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testMain() throws DocumentException,
			UnsupportedEncodingException, IOException {
		// String pathname = "D:/dev/workspace_home/SSH/src/beans.xml";
		String pathname = "E:/books/史/新唐书/001.htm";
		File xmlFile = new File(pathname);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new InputStreamReader(new FileInputStream(
				xmlFile), "GB2312"));
		Element root = doc.getRootElement();
		System.out.println(root.getStringValue());
	}
}
