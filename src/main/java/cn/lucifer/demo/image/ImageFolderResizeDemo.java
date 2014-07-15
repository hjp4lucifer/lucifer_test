package cn.lucifer.demo.image;

import java.io.File;
import java.io.FileFilter;

import org.junit.Test;

import cn.lucifer.util.ImageUtils;

public class ImageFolderResizeDemo {
	@Test
	public void testMain(String[] args) {
		// String basePath = "E:/info/头像";
		String basePath = "E:/info/头像/1/1";
		File baseFolder = new File(basePath);
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

			String newFolderPath = basePath + "/" + System.currentTimeMillis();
			File newFolder = new File(newFolderPath);
			newFolder.mkdir();
			for (File img : images) {
				String newFilePath = newFolderPath + "/" + img.getName();
				File to = new File(newFilePath);
				ImageUtils.resize(img, to, 100, 100);
			}
		}
		System.out.println("ok!");
	}
}
