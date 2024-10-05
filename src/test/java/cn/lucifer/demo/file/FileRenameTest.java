package cn.lucifer.demo.file;

import cn.lucifer.demo.string.RegexDemo;
import cn.lucifer.util.StrUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class FileRenameTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

	@Test
	public void bt_xltd_rename_2() {
		String basePath = "M:\\迅雷下载\\【超重磅】OF绝美反差女神【little_sula】更新至10月精品收录89P+37V【无水原版】嫩出天际！！高价付费订阅\\P";
		File folder = new File(basePath);
		assertTrue(folder.exists());

		String suffix = ".bt.xltd";

		List<File> handleFileList = new ArrayList<>();
		for (int i = 73; i <= 74; i++) {
			String fn = String.format("guochan2048.com-(%d).jpg.bt.xltd", i);
			handleFileList.add(new File(folder, fn));
		}

		assertTrue(handleFileList.size() > 0);

		for (File f : handleFileList) {
			if (!f.exists()) {
				continue;
			}
			File newF = new File(folder, StringUtils.removeEnd(f.getName(), suffix));
			logger.info(newF.getAbsolutePath());
			f.renameTo(newF);
		}
	}

	@Test
	public void remove_2_parent() {
		String basePath = "E:\\mv\\Korea";
		File folder = new File(basePath);
		assertTrue(folder.exists());

		File[] directoryList = folder.listFiles(pathname -> pathname.isDirectory());

		assertNotEquals(0, directoryList.length);

		for (File dir : directoryList) {
			for (File f : dir.listFiles()) {
				File newF = new File(folder, f.getName());
				logger.info(newF.getAbsolutePath());
				f.renameTo(newF);
			}
		}
	}

}
