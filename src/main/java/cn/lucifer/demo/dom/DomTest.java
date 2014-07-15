/**
 * 
 */
package cn.lucifer.demo.dom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * (也是要求得非常严格)
 * 
 * @author Lucifer
 * 
 */
public class DomTest {

	@Test
	public void mainTest() throws UnsupportedEncodingException, IOException,
			ParserConfigurationException, SAXException {
		String pathname = "E:/books/史/新唐书/001.htm";
		File xmlFile = new File(pathname);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
				xmlFile), "GB2312");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(reader));
		NodeList nl = doc.getElementsByTagName("body");
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			System.out.println(node.getTextContent());
		}
	}

}
