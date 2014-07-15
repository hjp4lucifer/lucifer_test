package cn.lucifer.demo.image;

import java.awt.Image;
import java.io.File;

import org.junit.Test;

import cn.lucifer.util.Images;


public class ResizeDemo {

	@Test
	public void testMain(String[] args) {
		String basePath = "D:/test/a/";
		File file1 = new File(basePath + "4dc4286300ce1769d550cc93.jpg");
		File file2 = new File(basePath + "4dc4359220321769df48f6e0.jpg");
		File o1 = new File(basePath + "o1.jpg"), o2 = new File(basePath
				+ "o2.jpg");
		Images.resize(file1, o1, 612, -1, Image.SCALE_SMOOTH);
		Images.resize(file2, o2, 612, -1, Image.SCALE_SMOOTH);
		System.out.println("ok!");
	}

}
