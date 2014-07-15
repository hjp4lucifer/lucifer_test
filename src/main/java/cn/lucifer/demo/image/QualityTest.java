/**
 * 
 */
package cn.lucifer.demo.image;

import java.io.File;

import org.junit.Test;

import cn.lucifer.util.ImageUtils;

/**
 * @author Lucifer
 * 
 */
public class QualityTest {
	@Test
	public void testMain(String[] args) {
		String path = "D:/test/";
		String pathname = path + "aaa.jpg";
		File image = new File(pathname);
		File to = new File(path + "95.jpg");
		ImageUtils.resize(image, to, 480, 640);// 480x640, 210x280
		System.out.println("ok!");
	}

}
