package cn.lucifer.demo.file.time;

/**
 * 一次批量改时间的统计结果。<br>
 * <br>
 * 三个计数的含义：
 * <ul>
 * <li>成功：本次写入了时间戳的文件数。试运行模式下统计的是"本应写入"的数量</li>
 * <li>跳过：三个时间已经等于目标时间, 未产生写入的文件数</li>
 * <li>失败：写入过程中抛出异常的文件数</li>
 * </ul>
 */
public class ChangeTimeResult {

	private int successCount;

	private int skipCount;

	private int failCount;

	public void addSuccess() {
		successCount++;
	}

	public void addSkip() {
		skipCount++;
	}

	public void addFail() {
		failCount++;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public int getSkipCount() {
		return skipCount;
	}

	public int getFailCount() {
		return failCount;
	}

	/**
	 * @return 成功 + 跳过 + 失败, 即本次检查过的文件总数
	 */
	public int getTotalCount() {
		return successCount + skipCount + failCount;
	}

	@Override
	public String toString() {
		return String.format("成功=%d, 跳过=%d, 失败=%d", successCount, skipCount, failCount);
	}

}
