package cn.lucifer.demo.file;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;

import static org.junit.Assert.*;

public class FileRenameTest {

	@Test
	public void bt_xltd_rename() {
		String basePath = "M:\\limit\\lspback.com极品辣妹Leeesovely李素英写真集A [195P1V-3.58GB]";
		File folder = new File(basePath);
		assertTrue(folder.exists());

		String suffix = ".bt.xltd";

		File[] xltdFileList = folder.listFiles((dir, name) -> name.endsWith(suffix));
		assertTrue(xltdFileList.length > 0);

		for (File f : xltdFileList) {
			File newF = new File(folder, StringUtils.removeEnd(f.getName(), suffix));
			System.out.println(newF.getAbsolutePath());
			f.renameTo(newF);
		}
	}
}
