package cn.lucifer.demo.file;

import java.io.File;
import java.io.IOException;

public class MoveFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		String srcFolderPath = "E:/sd_cache/img/20140227-01";
		String desFolderPath = "E:/sd_cache/img/20140306-01";
		File srcFolder = new File(srcFolderPath);
		File desFolder = new File(desFolderPath);

		moveNoCoverCheck(srcFolder, desFolder);
	}

	/**
	 * 移动, 且不覆盖
	 * 
	 * @param srcFolder
	 * @param desFolder
	 */
	public static void moveNoCoverCheck(File srcFolder, File desFolder)
			throws IOException {
		// 标准化文件路径
		String srcPath = srcFolder.getPath();
		String desPath = desFolder.getPath();

		File[] srcFiles = srcFolder.listFiles();
		for (File src : srcFiles) {
			if (src.isDirectory()) {
				File newDestFolder = getDestFolder(src, srcPath, desPath);
				if (newDestFolder == null) {
					continue;
				}
				moveNoCoverCheck(src, newDestFolder);
				continue;
			}
			moveNoCover(src, srcPath, desPath);
		}
	}

	public static File getDestFolder(File srcFile, String srcPath,
			String desPath) {
		String desFilePath = srcFile.getPath().replace(srcPath, desPath);
		// System.out.println(desFilePath);
		File dest = new File(desFilePath);
		if (dest.exists()) {
			return dest;
		}

		// dest.mkdirs();
		moveNoCover(srcFile, srcPath, desPath);
		return null;
	}

	public static void moveNoCover(File srcFile, String srcPath, String desPath) {
		String desFilePath = srcFile.getPath().replace(srcPath, desPath);
		File dest = new File(desFilePath);
		if (dest.exists()) {
			return;
		}
		System.out.println("move to : " + desFilePath);
		srcFile.renameTo(dest);
	}

}
