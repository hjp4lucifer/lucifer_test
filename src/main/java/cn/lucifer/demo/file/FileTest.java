/**
 * 
 */
package cn.lucifer.demo.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Lucifer
 * 
 */
public class FileTest {

	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));
		System.out.println(new File("D:/dev/workspace/Demo").getParent());
		System.out.println(new File("D:/dev/workspace/Demo").getName());
		System.out.println(DigestUtils.md5Hex(new FileInputStream(
				"C:/test/Cover1.apk")));
	}
}
