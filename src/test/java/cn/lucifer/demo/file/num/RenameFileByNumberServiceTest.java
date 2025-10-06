package cn.lucifer.demo.file.num;

import cn.lucifer.util.StrUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

public class RenameFileByNumberServiceTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private RenameFileByNumberService service;

	@Before
	public void setUp() throws Exception {
		service = new RenameFileByNumberService();
	}

	@Test
	public void Yeah_rename_with_start() {
		final String basePath = "M:\\limit\\▌Yeha▌\\[Pure Media] VOL.229 Yeha (예하)";
		File folder = new File(basePath);
		final String fnFormat = "[Pure Media] VOL.229 Yeha (예하)-%03d.jpg";
		for (int i = 52; i >= 36; i--) {
			File f = new File(folder, String.format(fnFormat, i));
			if (!f.exists()) {
				continue;
			}
			File dest = new File(folder, String.format(fnFormat, i + 1));
			logger.info("fn={} , newFn={}", f.getName(), dest.getName());
			f.renameTo(dest);
		}

	}

	@Test
	public void Yeha_rename() {
//		final String basePath = "M:\\limit\\▌Yeha▌";
//		final String basePath = "L:\\limit_pic\\▌Pure Media▌（精品收集）";
		final String basePath = "M:\\limit\\▌Pure Media▌（精品收集）\\Vol.127 Son Ye-Eun (손예은)";
		// 分析文件的数字构成
		File folder = new File(basePath);
		assertTrue(folder.exists());

		List<String> excludeFolderList = Lists.newArrayList(
				"Vol.169 Aram",
				"Vol.229 Yeha"
		);

		handleFolder(folder, excludeFolderList);
	}

	private void handleFolder(File folder, List<String> excludeFolderList) {
		assertTrue(folder.exists());
		Pair<File[], File[]> pair = service.leftFileRightFolder(folder, excludeFolderList);

		// 不含子目录
		File[] allFiles = pair.getLeft();
		File[] allDirectory = pair.getRight();

		if (null != allFiles && allFiles.length > 0) {
			service.analysisFormat(allFiles);
		}
		if (null != allDirectory && allDirectory.length > 0) {
			for (File d : allDirectory) {
				handleFolder(d, excludeFolderList);
			}
		}
	}

}