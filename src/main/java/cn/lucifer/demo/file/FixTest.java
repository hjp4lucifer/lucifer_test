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

/**
 * @author Lucifer
 * 
 */
public class FixTest {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String path = "D:/test/";
		File file = new File(path + "ico.txt");
		File outFile = new File(path+ System.currentTimeMillis()+".txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

		String line;
		while ((line = reader.readLine()) != null) {
			writer.write(line);
		}
		writer.flush();
		writer.close();
		reader.close();
		System.out.println("ok!");
	}
}
