package cn.lucifer.demo.file.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 图片文件收集器：负责"按 basePath 类型分流 + 递归遍历 + 扩展名筛选 + 排除目录"。<br>
 * <br>
 * 目录模式只收集 jpg/jpeg/png（大小写不敏感），单文件模式原样返回该文件且不筛选扩展名。<br>
 * 改时间工具与文件夹陈旧度检查工具共用本类，避免两处规则各自演化。
 */
public class ImageFileCollector {

	/** 可识别的图片扩展名, 比较时不区分大小写 */
	private static final Set<String> IMAGE_EXTENSION_SET = Set.of("jpg", "jpeg", "png");

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 收集目标文件。目录则递归遍历, 普通文件则只返回它自己。
	 *
	 * @param basePath          目录或单个文件, 不能为 null 且必须存在
	 * @param excludeFolderList 目录模式下需要跳过的子目录名, 可为 null
	 * @return 目标文件列表
	 */
	public List<File> collect(File basePath, List<String> excludeFolderList) {
		if (null == basePath) {
			throw new IllegalArgumentException("basePath不能为null");
		}
		if (!basePath.exists()) {
			throw new IllegalArgumentException("basePath不存在: " + basePath.getAbsolutePath());
		}

		List<File> resultList = new ArrayList<>();
		if (basePath.isDirectory()) {
			collectFolderFileList(basePath, excludeFolderList, resultList);
		} else {
			// 单文件是显式指定的目标, 不筛选扩展名
			resultList.add(basePath);
		}
		return resultList;
	}

	/**
	 * 递归收集目录下的图片文件。
	 */
	private void collectFolderFileList(File folder, List<String> excludeFolderList, List<File> resultList) {
		File[] subFileList = folder.listFiles();
		if (null == subFileList || 0 == subFileList.length) {
			return;
		}

		for (File subFile : subFileList) {
			if (subFile.isDirectory()) {
				if (isExcludeFolder(subFile, excludeFolderList)) {
					logger.debug("跳过排除目录, folder={}", subFile.getAbsolutePath());
					continue;
				}
				collectFolderFileList(subFile, excludeFolderList, resultList);
				continue;
			}
			if (isImageFile(subFile)) {
				resultList.add(subFile);
			}
		}
	}

	/**
	 * 判断是否是需要跳过的子目录。
	 */
	private boolean isExcludeFolder(File folder, List<String> excludeFolderList) {
		if (null == excludeFolderList) {
			return false;
		}
		for (String excludeFolder : excludeFolderList) {
			if (excludeFolder.equals(folder.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 按扩展名判断是否为需要处理的图片, 不区分大小写。
	 */
	private boolean isImageFile(File file) {
		return IMAGE_EXTENSION_SET.contains(getExtension(file.getName()));
	}

	/**
	 * 取文件名的扩展名, 没有扩展名时返回空串。<br>
	 * 用"最后一个点"判断, 因此 {@code a.jpg.bak} 的扩展名是 {@code bak}, 不会被当成图片。
	 */
	private static String getExtension(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index <= 0 || index == fileName.length() - 1) {
			return "";
		}
		return fileName.substring(index + 1).toLowerCase(Locale.ROOT);
	}

}
