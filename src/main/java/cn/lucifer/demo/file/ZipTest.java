/**
 * 
 */
package cn.lucifer.demo.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class ZipTest {

	@Test
	public void testMain() throws IOException {
		File file = new File("D:/test/zipTTT.zip");
		ZipOutputStream outputStream = new ZipOutputStream(
				new FileOutputStream(file));
		ZipEntry zipEntry = new ZipEntry("01.gif");
		outputStream.putNextEntry(zipEntry);
		File infile = new File("D:/test/demo.gif");
		IOUtils.copy(new FileInputStream(infile), outputStream);
		outputStream.closeEntry();
		outputStream.flush();
		outputStream.close();
	}

}
