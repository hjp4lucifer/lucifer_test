package cn.lucifer.demo.file.time;

import java.io.File;
import java.time.LocalDate;

/**
 * 一条"近期有更新"的命中记录。<br>
 * <br>
 * 字段含义：
 * <ul>
 * <li>target：命中的目标，目录模式下是目录，单文件模式下是那个文件</li>
 * <li>hitFile：让这个目标命中的那个文件</li>
 * <li>hitDate：hitFile 的有效时间，即其 {@code min(创建时间, 修改时间)} 的日期部分</li>
 * </ul>
 * 一个目标只会输出一条记录：只要有一个文件落在范围内就够，其余文件不再读取。
 */
public class FolderRecentReport {

	private final File target;

	private final File hitFile;

	private final LocalDate hitDate;

	public FolderRecentReport(File target, File hitFile, LocalDate hitDate) {
		this.target = target;
		this.hitFile = hitFile;
		this.hitDate = hitDate;
	}

	public File getTarget() {
		return target;
	}

	public File getHitFile() {
		return hitFile;
	}

	public LocalDate getHitDate() {
		return hitDate;
	}

	@Override
	public String toString() {
		return String.format("近期有更新, hit=%s (t=%s), target=%s",
				hitFile.getName(), hitDate, target.getAbsolutePath());
	}

}
