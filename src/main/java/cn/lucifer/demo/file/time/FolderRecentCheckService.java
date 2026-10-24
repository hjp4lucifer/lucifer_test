package cn.lucifer.demo.file.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件夹新鲜度检查：找出"最近 n 个月内还有图片更新过"的目录。<br>
 * <br>
 * 判定规则：
 * <ul>
 * <li>截止日 = 参考日期减 n 个月</li>
 * <li>目录模式下逐层判定，每个<b>直接含图片</b>的目录各自独立判断；纯容器目录不参与</li>
 * <li>目录内<b>任意一个</b>图片的有效时间晚于截止日，就判定该目录命中，并立即停止扫描该目录剩余文件</li>
 * <li>单个文件的有效时间 = {@code min(创建时间, 修改时间)}，截断到日；访问时间不参与</li>
 * <li>{@code basePath} 是普通文件时，只判定该文件本身，不筛选扩展名</li>
 * </ul>
 * <br>
 * 纯只读：不会修改任何文件的时间戳。
 */
public class FolderRecentCheckService {

	/** 默认的"近期"范围(月) */
	public static final int DEFAULT_RECENT_MONTHS = 6;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** 目标文件的收集逻辑, 与改时间工具共用 */
	private final ImageFileCollector imageFileCollector = new ImageFileCollector();

	/** "近期"的月数范围 */
	private int recentMonths = DEFAULT_RECENT_MONTHS;

	/** 参考日期；为 null 时取当天 */
	private LocalDate referenceDate;

	/**
	 * 执行检查。
	 *
	 * @param basePath          目录或单个文件, 不能为 null 且必须存在
	 * @param excludeFolderList 目录模式下需要跳过的子目录名, 可为 null
	 * @return 全部命中项, 按目标路径排序；没有命中时返回空列表
	 * @throws IOException 读取文件属性失败
	 */
	public List<FolderRecentReport> check(File basePath, List<String> excludeFolderList) throws IOException {
		// 先校验月数, 保证在遍历任何文件之前就失败
		validateRecentMonths();
		LocalDate today = null != referenceDate ? referenceDate : LocalDate.now();
		LocalDate cutoffDate = today.minusMonths(recentMonths);

		List<File> fileList = imageFileCollector.collect(basePath, excludeFolderList);
		logger.info("开始检查, basePath={}, 参数={}", basePath.getAbsolutePath(),
				buildModeDesc(cutoffDate, fileList.size()));

		List<FolderRecentReport> reportList = basePath.isDirectory()
				? checkFolderList(fileList, cutoffDate)
				: checkSingleFile(basePath, cutoffDate);

		// 按路径排序, 保证多次执行输出顺序一致(命中文件本身取决于遍历顺序, 不适合用来排序)
		reportList.sort(Comparator.comparing(report -> report.getTarget().getAbsolutePath()));

		for (FolderRecentReport report : reportList) {
			logger.warn("{}", report);
		}
		logger.info("检查结束, basePath={}, 命中数={}", basePath.getAbsolutePath(), reportList.size());
		return reportList;
	}

	/**
	 * 目录模式：按父目录分组, 每组只要有一个文件命中就收工。
	 */
	private List<FolderRecentReport> checkFolderList(List<File> fileList, LocalDate cutoffDate) throws IOException {
		Map<String, List<File>> path2FileList = new LinkedHashMap<>();
		for (File file : fileList) {
			File parentFolder = file.getParentFile();
			if (null == parentFolder) {
				continue;
			}
			String parentPath = parentFolder.getAbsolutePath();
			path2FileList.computeIfAbsent(parentPath, key -> new ArrayList<>()).add(file);
		}

		List<FolderRecentReport> reportList = new ArrayList<>();
		for (Map.Entry<String, List<File>> entry : path2FileList.entrySet()) {
			File folder = new File(entry.getKey());
			FolderRecentReport report = findHitReport(folder, entry.getValue(), cutoffDate);
			if (null != report) {
				reportList.add(report);
			}
		}
		return reportList;
	}

	/**
	 * 单文件模式：目标就是这个文件本身, 不看它的父目录。
	 */
	private List<FolderRecentReport> checkSingleFile(File file, LocalDate cutoffDate) throws IOException {
		FolderRecentReport report = findHitReport(file, Collections.singletonList(file), cutoffDate);
		if (null == report) {
			return new ArrayList<>();
		}
		List<FolderRecentReport> reportList = new ArrayList<>();
		reportList.add(report);
		return reportList;
	}

	/**
	 * 在 fileList 里找第一个命中截止日的文件。
	 *
	 * @return 命中时返回记录, 一个都没有则返回 null
	 */
	private FolderRecentReport findHitReport(File target, List<File> fileList, LocalDate cutoffDate)
			throws IOException {
		for (File file : fileList) {
			LocalDate fileDate = readDate(file);
			if (!fileDate.isAfter(cutoffDate)) {
				continue;
			}
			// 命中一个就够了, 该目录剩余文件不再读取属性
			return new FolderRecentReport(target, file, fileDate);
		}
		return null;
	}

	/**
	 * 读取一个文件的有效时间：min(创建时间, 修改时间), 截断到日。
	 */
	private static LocalDate readDate(File file) throws IOException {
		BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
		return earlierDate(attributes.creationTime(), attributes.lastModifiedTime());
	}

	/**
	 * 取两个时间中较早的日期, 比较前截断到日。
	 */
	private static LocalDate earlierDate(FileTime first, FileTime second) {
		LocalDate firstDate = toLocalDate(first);
		LocalDate secondDate = toLocalDate(second);
		return firstDate.isBefore(secondDate) ? firstDate : secondDate;
	}

	/**
	 * 按本地时区把 FileTime 转成日期。
	 */
	private static LocalDate toLocalDate(FileTime fileTime) {
		return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault()).toLocalDate();
	}

	private void validateRecentMonths() {
		if (recentMonths <= 0) {
			throw new IllegalArgumentException("recentMonths必须大于0, 当前=" + recentMonths);
		}
	}

	private String buildModeDesc(LocalDate cutoffDate, int totalCount) {
		return String.format("recentMonths=%d, 截止日=%s, 待检查文件数=%d", recentMonths, cutoffDate, totalCount);
	}

	public int getRecentMonths() {
		return recentMonths;
	}

	public void setRecentMonths(int recentMonths) {
		this.recentMonths = recentMonths;
	}

	public LocalDate getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(LocalDate referenceDate) {
		this.referenceDate = referenceDate;
	}

}
