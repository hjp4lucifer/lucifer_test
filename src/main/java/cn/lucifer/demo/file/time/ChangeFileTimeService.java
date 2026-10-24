package cn.lucifer.demo.file.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

/**
 * 把文件的创建时间、修改时间、访问时间统一写成同一个值。<br>
 * <br>
 * 两种取值模式，由 {@link #setNormalizeToMin(boolean)} 切换，默认是对齐模式：
 * <ul>
 * <li>对齐模式：目标时间取创建时间与修改时间中较早的一个，访问时间不参与比较</li>
 * <li>指定模式：目标时间取调用方传入的 {@code yyyy-MM-dd HH:mm:ss} 字符串，按本地时区解释</li>
 * </ul>
 * <br>
 * 处理入口由 basePath 的类型自动分流：目录则递归收集 jpg/jpeg/png，普通文件则只处理该文件本身。<br>
 * <br>
 * 注意：写时间戳是不可逆操作，且写完之后任何一次复制都会把创建时间重置成"复制那一刻"。<br>
 * 使用方法见 {@code ChangeFileTimeServiceTest}。
 */
public class ChangeFileTimeService {

	/** 严格解析用的格式化器, 必须用 uuuu 才能配合 STRICT 校验 */
	private static final DateTimeFormatter STRICT_TIME_FORMATTER = DateTimeFormatter
			.ofPattern("uuuu-MM-dd HH:mm:ss")
			.withResolverStyle(ResolverStyle.STRICT);

	/** 日志与报错信息里展示用的格式化器 */
	private static final DateTimeFormatter DISPLAY_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/** 对外提示的格式说明 */
	public static final String TIME_PATTERN_DESC = "yyyy-MM-dd HH:mm:ss";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** 目标文件的收集逻辑, 与陈旧度检查工具共用 */
	private final ImageFileCollector imageFileCollector = new ImageFileCollector();

	/** true=对齐模式(默认), false=指定模式 */
	private boolean normalizeToMin = true;

	/** 指定模式下的目标时间, 格式见 {@link #TIME_PATTERN_DESC} */
	private String fixedTime;

	/** true=只报告不落盘, 默认开启 */
	private boolean dryRun = true;

	/**
	 * 处理 basePath 指向的目标。<br>
	 * 目录则递归收集图片, 普通文件则只处理该文件。
	 *
	 * @param basePath          目录或单个文件, 不能为 null 且必须存在
	 * @param excludeFolderList 目录模式下需要跳过的子目录名, 可为 null
	 * @return 本次处理的统计结果
	 * @throws IOException 遍历目录时出错
	 */
	public ChangeTimeResult handleBasePath(File basePath, List<String> excludeFolderList) throws IOException {
		// 先校验取值模式, 保证在动任何文件之前就失败
		FileTime fixedTarget = resolveFixedTarget();

		List<File> targetFileList = collectTargetFiles(basePath, excludeFolderList);
		logger.info("开始处理, basePath={}, 参数={}", basePath.getAbsolutePath(),
				buildModeDesc(targetFileList.size()));

		ChangeTimeResult result = new ChangeTimeResult();
		for (File file : targetFileList) {
			try {
				handleFile(file, fixedTarget, result);
			} catch (IOException e) {
				result.addFail();
				logger.info("处理失败, file={}, reason={}", file.getAbsolutePath(), e.toString());
			}
		}

		logger.info("处理结束, basePath={}, {}", basePath.getAbsolutePath(), result);
		return result;
	}

	/**
	 * 收集本次要处理的文件。<br>
	 * 目录模式只收集 jpg/jpeg/png, 单文件模式不筛选扩展名。
	 *
	 * @param basePath          目录或单个文件
	 * @param excludeFolderList 目录模式下需要跳过的子目录名, 可为 null
	 * @return 待处理文件列表
	 */
	public List<File> collectTargetFiles(File basePath, List<String> excludeFolderList) {
		return imageFileCollector.collect(basePath, excludeFolderList);
	}

	/**
	 * 严格解析 {@code yyyy-MM-dd HH:mm:ss} 形式的时间字符串。
	 *
	 * @param timeStr 时间字符串
	 * @return 对应的 FileTime
	 */
	public static FileTime parseFixedTime(String timeStr) {
		if (null == timeStr || timeStr.trim().isEmpty()) {
			throw new IllegalArgumentException("时间不能为空, 期望格式: " + TIME_PATTERN_DESC);
		}
		String trimmed = timeStr.trim();
		try {
			LocalDateTime localDateTime = LocalDateTime.parse(trimmed, STRICT_TIME_FORMATTER);
			Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
			return FileTime.from(instant);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("时间格式非法: " + trimmed + ", 期望格式: " + TIME_PATTERN_DESC, e);
		}
	}

	/**
	 * 处理单个文件：算出目标时间, 判断是否已对齐, 再决定是否写入。
	 */
	private void handleFile(File file, FileTime fixedTarget, ChangeTimeResult result) throws IOException {
		Path path = file.toPath();
		// 一次读全三个时间, 避免多次系统调用
		BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
		FileTime creationTime = attributes.creationTime();
		FileTime modifiedTime = attributes.lastModifiedTime();
		FileTime accessTime = attributes.lastAccessTime();

		FileTime targetTime = null != fixedTarget ? fixedTarget : earlierOf(creationTime, modifiedTime);

		String detail = buildDetail(file, creationTime, modifiedTime, accessTime, targetTime);
		String tag = dryRun ? "DRY-RUN" : "CHANGE";

		// 已对齐判断只看创建时间与修改时间: 访问时间会被外部程序刷新, 让它参与会导致反复重写
		if (creationTime.equals(targetTime) && modifiedTime.equals(targetTime)) {
			result.addSkip();
			logger.info("{} 跳过(已对齐) {}", tag, detail);
			return;
		}

		logger.info("{} {}", tag, detail);
		if (!dryRun) {
			writeThreeTimes(path, targetTime);
		}
		result.addSuccess();
	}

	/**
	 * 把同一个时间写入创建时间、修改时间、访问时间三处。<br>
	 * 参数顺序是 (修改, 访问, 创建), 与实际写入的槽位不同, 不要凭直觉调整。
	 */
	private void writeThreeTimes(Path path, FileTime targetTime) throws IOException {
		BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class);
		view.setTimes(targetTime, targetTime, targetTime);
	}


	/**
	 * 取两个时间中较早的一个。
	 */
	private static FileTime earlierOf(FileTime first, FileTime second) {
		return first.compareTo(second) <= 0 ? first : second;
	}

	/**
	 * 校验并解析指定模式的目标时间; 对齐模式返回 null。
	 */
	private FileTime resolveFixedTarget() {
		if (normalizeToMin) {
			return null;
		}
		if (null == fixedTime || fixedTime.trim().isEmpty()) {
			throw new IllegalArgumentException("关闭对齐模式时必须提供时间, 期望格式: " + TIME_PATTERN_DESC);
		}
		return parseFixedTime(fixedTime);
	}

	/**
	 * 拼装本次运行的参数说明。
	 */
	private String buildModeDesc(int totalCount) {
		return String.format("dryRun=%s, normalizeToMin=%s, 待处理文件数=%d", dryRun, normalizeToMin, totalCount);
	}

	/**
	 * 拼装一行可读的处理信息。
	 */
	private String buildDetail(File file, FileTime creationTime, FileTime modifiedTime, FileTime accessTime,
			FileTime targetTime) {
		return String.format("file=%s, creation=%s, modified=%s, access=%s, target=%s",
				file.getAbsolutePath(),
				DISPLAY_TIME_FORMATTER.format(toLocalDateTime(creationTime)),
				DISPLAY_TIME_FORMATTER.format(toLocalDateTime(modifiedTime)),
				DISPLAY_TIME_FORMATTER.format(toLocalDateTime(accessTime)),
				DISPLAY_TIME_FORMATTER.format(toLocalDateTime(targetTime)));
	}

	/**
	 * 按本地时区把 FileTime 转成可展示的 LocalDateTime。
	 */
	private static LocalDateTime toLocalDateTime(FileTime fileTime) {
		return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
	}

	public boolean isNormalizeToMin() {
		return normalizeToMin;
	}

	public void setNormalizeToMin(boolean normalizeToMin) {
		this.normalizeToMin = normalizeToMin;
	}

	public String getFixedTime() {
		return fixedTime;
	}

	public void setFixedTime(String fixedTime) {
		this.fixedTime = fixedTime;
	}

	public boolean isDryRun() {
		return dryRun;
	}

	public void setDryRun(boolean dryRun) {
		this.dryRun = dryRun;
	}

}
