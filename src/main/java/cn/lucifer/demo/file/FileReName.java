package cn.lucifer.demo.file;

import java.io.File;

public class FileReName {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String pathname = "E:/books/chenyuan";
		File folder = new File(pathname);
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					String name = file.getName();
					if (name.equals("000序章 相约.doc")
							|| name.equals("405终章 一曲千年.doc")) {
						continue;
					}
					if (name.indexOf("章") > 0) {
						String deleteString = name.substring(name.indexOf("章"),
								name.lastIndexOf(" ") + 1);
						// System.out.print("delete : " + deleteString + "  ");
						// System.out.print("old : " + name + "  ");
						name = name.replace(deleteString, "");
						// System.out.println(name);
						File renameFile = new File(pathname + "/" + name);
						file.renameTo(renameFile);
					}
				}
			}
		}
		System.out.println("ok!");
	}
}
