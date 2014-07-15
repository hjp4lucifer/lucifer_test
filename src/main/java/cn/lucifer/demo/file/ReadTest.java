/**
 * 
 */
package cn.lucifer.demo.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class ReadTest {

	@Test
	public void testMain() throws IOException {
		String path = "E:/数据库/db/renren.txt";
		File file = new File(path);
		File floder = new File(path + System.currentTimeMillis());
		floder.mkdir();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int lineCount = 0;
		BufferedWriter writer = createWriter(floder, lineCount);
		String line;
		while ((line = reader.readLine()) != null) {
			lineCount++;
			writer.write(line + "\r\n");
			if (lineCount % 60000 == 0) {
				System.out.println(lineCount);
				writer.flush();
				writer.close();
				writer = createWriter(floder, lineCount);
			}
		}
		reader.close();
		writer.flush();
		writer.close();
	}

	public BufferedWriter createWriter(File floder, int lineCount)
			throws IOException {
		return new BufferedWriter(new FileWriter(new File(
				floder.getAbsolutePath() + "/" + (lineCount / 60000) + ".txt")));
	}
}
