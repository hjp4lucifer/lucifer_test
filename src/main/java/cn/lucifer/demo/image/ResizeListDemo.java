package cn.lucifer.demo.image;

import java.io.File;
import java.io.FileFilter;

import org.junit.Test;

import cn.lucifer.util.ImageUtils;

public class ResizeListDemo {
	@Test
	public void testMain() {
		String basePath = "D:/test/b";
		File baseFolder = new File(basePath);
		String folderPath612 = basePath + "/612x612", folderPath500 = basePath
				+ "/500x500", folderPath400 = basePath + "/400x400";
		File folder612 = new File(folderPath612), folder500 = new File(
				folderPath500), folder400 = new File(folderPath400);
		folder612.mkdir();
		folder500.mkdir();
		folder400.mkdir();
		if (baseFolder.isDirectory()) {
			File[] images = baseFolder.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					String fileName = pathname.getName();
					if (fileName.indexOf(".jpg") > 0) {
						return true;
					} else {
						return false;
					}
				}
			});

			for (File img : images) {
				File img612 = new File(processImageName(folderPath612, img)), img500 = new File(
						processImageName(folderPath500, img)), img400 = new File(
						processImageName(folderPath400, img));
				ImageUtils.resize(img, img612, 612, -1);
				ImageUtils.resize(img, img500, 500, -1);
				ImageUtils.resize(img, img400, 400, -1);
			}
		}
		System.out.println("ok!");
	}

	public static String processImageName(String folderPath, File img) {
		return folderPath + "/" + img.getName();
	}

}
