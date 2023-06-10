package cn.lucifer.demo.file;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;

import static org.junit.Assert.*;

public class FileRenameTest {

	@Test
	public void bt_xltd_rename() {
		String basePath = "E:\\limit\\万人求购国际版抖音OnlyFans网红健身撸铁美女S级身材pup大胆自拍喜欢玩肛的女人\\P";
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
