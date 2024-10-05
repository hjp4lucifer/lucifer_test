package cn.lucifer.demo.file.num;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.*;

public class RenameFileByNumberServiceTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private RenameFileByNumberService service;

	@Before
	public void setUp() throws Exception {
		service = new RenameFileByNumberService();
	}

	@Test
	public void Yeha_rename() {
//		final String basePath = "M:\\limit\\▌Yeha▌";
		final String basePath = "";
		// 分析文件的数字构成
		File folder = new File(basePath);
		assertTrue(folder.exists());

		handleFolder(folder);
	}

	private void handleFolder(File folder) {
		assertTrue(folder.exists());
		Pair<File[], File[]> pair = service.leftFileRightFolder(folder);

		// 不含子目录
		File[] allFiles = pair.getLeft();
		File[] allDirectory = pair.getRight();

		if (null != allFiles && allFiles.length > 0) {
			service.analysisFormat(allFiles);
		}
		if (null != allDirectory && allDirectory.length > 0) {
			for (File d : allDirectory) {
				handleFolder(d);
			}
		}
	}

}