/**
 * 
 */
package cn.lucifer.demo.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class FileTest {

	@Test
	public void testMain() throws IOException {
		System.out.println(System.getProperty("user.dir"));
		System.out.println(new File("D:/workspace/lucifer_test").getParent());
		System.out.println(new File("D:/workspace/lucifer_test").getName());
//		System.out.println(DigestUtils.md5Hex(new FileInputStream(
//				"C:/test/Cover1.apk")));
	}
}
