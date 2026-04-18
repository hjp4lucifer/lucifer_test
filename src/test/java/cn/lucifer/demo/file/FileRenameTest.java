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

/**
 * 文件重命名和文件操作测试类
 * 提供批量文件重命名、文件移动等工具方法的测试用例
 */
public class FileRenameTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 批量移除文件名中的特定后缀
	 * 将指定目录下所有以 ".bt.xltd" 结尾的文件重命名，移除该后缀
	 */
	@Test
	public void bt_xltd_rename() {
		String basePath = "M:\\limit\\lspback.com极品辣妹Leeesovely李素英写真集A [195P1V-3.58GB]";
		File folder = new File(basePath);
		assertTrue(folder.exists());

		// 定义需要移除的后缀
		String suffix = ".bt.xltd";

		// 获取所有以指定后缀结尾的文件
		File[] xltdFileList = folder.listFiles((dir, name) -> name.endsWith(suffix));
		assertTrue(xltdFileList.length > 0);

		// 遍历文件并重命名（移除后缀）
		for (File f : xltdFileList) {
			File newF = new File(folder, StringUtils.removeEnd(f.getName(), suffix));
			System.out.println(newF.getAbsolutePath());
			f.renameTo(newF);
		}
	}

	/**
	 * 批量重命名指定范围的文件
	 * 针对特定命名模式的文件，按照指定编号范围批量移除后缀
	 * 示例：处理 guochan2048.com-(73).jpg.bt.xltd 到 guochan2048.com-(74).jpg.bt.xltd
	 */
	@Test
	public void bt_xltd_rename_2() {
		String basePath = "M:\\迅雷下载\\【超重磅】OF绝美反差女神【little_sula】更新至10月精品收录89P+37V【无水原版】嫩出天际！！高价付费订阅\\P";
		File folder = new File(basePath);
		assertTrue(folder.exists());

		// 定义需要移除的后缀
		String suffix = ".bt.xltd";

		// 根据编号范围构建待处理文件列表
		List<File> handleFileList = new ArrayList<>();
		for (int i = 73; i <= 74; i++) {
			String fn = String.format("guochan2048.com-(%d).jpg.bt.xltd", i);
			handleFileList.add(new File(folder, fn));
		}

		assertTrue(handleFileList.size() > 0);

		// 遍历文件并重命名，跳过不存在的文件
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
	public void move_file_to_parent_and_remove_spaces() {
		String basePath = "E:\\book\\400+本高质量完本合集\\14、轻小说";
		move_file_to_parent(basePath);
		remove_spaces_from_filename(basePath);
	}

	/**
	 * 将子目录中的所有文件移动到父目录
	 * 遍历指定目录下的所有子文件夹，将其中的所有文件移动到当前目录
	 * 注意：如果存在同名文件，后移动的文件会覆盖先移动的文件
	 */
	@Test
	public void move_file_to_parent() {
		String basePath = "E:\\book\\400+本高质量完本合集\\15、女频";
		move_file_to_parent(basePath);
	}

	private void move_file_to_parent(String basePath) {
		File folder = new File(basePath);
		assertTrue(folder.exists());

		// 获取所有子文件夹
		File[] directoryList = folder.listFiles(pathname -> pathname.isDirectory());

		assertNotEquals(0, directoryList.length);

		// 遍历每个子文件夹，将其中所有文件移动到父目录
		for (File dir : directoryList) {
			for (File f : dir.listFiles()) {
				File newF = new File(folder, f.getName());
				logger.info(newF.getAbsolutePath());
				f.renameTo(newF);
			}
		}
	}


	/**
	 * 批量移除文件名中的空格
	 * 将指定目录下文件名中包含空格的文件重命名，移除或替换空格
	 */
	@Test
	public void remove_spaces_from_filename() {
		String basePath = "E:\\book\\400+本高质量完本合集\\15、女频";
		remove_spaces_from_filename(basePath);
	}

	private void remove_spaces_from_filename(String basePath) {
		File folder = new File(basePath);
		assertTrue(folder.exists());

		// 获取所有文件名包含空格的文件
		File[] filesWithSpaces = folder.listFiles((dir, name) -> name.contains(" "));

		if (filesWithSpaces != null) {
			// 遍历并重命名
			for (File f : filesWithSpaces) {
				// 替换或删除空格
				String newName = f.getName().replace(" ", "");  // 或 replace(" ", "_")
				File newF = new File(folder, newName);
				logger.info("重命名: {} -> {}", f.getName(), newName);
				f.renameTo(newF);
			}
		}
	}


}
