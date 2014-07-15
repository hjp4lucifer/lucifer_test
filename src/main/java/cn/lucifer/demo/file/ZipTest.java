/**
 * 
 */
package cn.lucifer.demo.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * @author Lucifer
 * 
 */
public class ZipTest {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis(), end;
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
		end = System.currentTimeMillis();
		System.out.println(end - start);
	}

}
