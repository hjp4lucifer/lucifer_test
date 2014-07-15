package cn.lucifer.demo.image;

import java.awt.Image;
import java.io.File;

import org.junit.Test;

import cn.lucifer.util.Images;

public class ImageResize {
	@Test
	public void testMain() {
		String basePath = "D:/test/";
		File file = new File(basePath + "IMG_1344.jpg");

		File file_SCALE_AREA_AVERAGING = new File(basePath
				+ "SCALE_AREA_AVERAGING.jpg"); // Image.SCALE_AREA_AVERAGING;
		File file_SCALE_DEFAULT = new File(basePath + "SCALE_DEFAULT.jpg");// Image.SCALE_DEFAULT;
		File file_SCALE_FAST = new File(basePath + "SCALE_FAST.jpg");// Image.SCALE_FAST;
		File file_SCALE_REPLICATE = new File(basePath + "SCALE_REPLICATE.jpg");// Image.SCALE_REPLICATE;
		File file_SCALE_SMOOTH = new File(basePath + "SCALE_SMOOTH.jpg");// Image.SCALE_SMOOTH;

		Images.resize(file, file_SCALE_AREA_AVERAGING, 400, -1,
				Image.SCALE_AREA_AVERAGING);
		Images.resize(file, file_SCALE_DEFAULT, 400, -1, Image.SCALE_DEFAULT);
		Images.resize(file, file_SCALE_FAST, 400, -1, Image.SCALE_FAST);
		Images.resize(file, file_SCALE_REPLICATE, 400, -1,
				Image.SCALE_REPLICATE);
		Images.resize(file, file_SCALE_SMOOTH, 400, -1, Image.SCALE_SMOOTH);

		System.out.println("ok!");

	}
}
