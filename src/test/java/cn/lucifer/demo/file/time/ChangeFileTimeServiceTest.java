package cn.lucifer.demo.file.time;

import com.google.common.collect.Lists;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * {@link ChangeFileTimeService} 的测试类。<br>
 * <br>
 * <b>本工具的定位与限制：</b>
 * <ol>
 * <li>应该是整条流水线的<b>最后一步</b>。之后任何一次复制（拷到别的盘、同步到 NAS）都会把创建时间重置成"复制那一刻"，
 * 前面的活就白干了；移动与重命名不影响时间戳。</li>
 * <li>只在 Windows + NTFS 上验证过。exFAT / FAT32 的时间精度不同（修改与创建 2 秒、访问时间只有天级），
 * 在那些盘上行为不保证。</li>
 * <li>写时间戳是不可逆操作，原值一旦覆盖就找不回来，所以 {@code dryRun} 默认开启。</li>
 * </ol>
 */
public class ChangeFileTimeServiceTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private ChangeFileTimeService service;

	@Before
	public void setUp() throws Exception {
		service = new ChangeFileTimeService();
	}

	// ------------------------------------------------------------------ 手动执行入口

	/**
	 * 手动执行入口：改下面几个参数, 然后直接运行本方法即可。<br>
	 * <br>
	 * basePath 指向<b>目录</b>就递归处理其中的 jpg/jpeg/png；指向<b>单个文件</b>就只处理那一个, 不看扩展名。<br>
	 * <b>注意</b>：{@code dryRun} 改成 false 会真的写入时间戳, 且不可撤销, 建议先用 true 看一遍日志。<br>
	 * 路径不存在时自动跳过, 不会让整个测试类报错。
	 */
	@Test
	public void Yeha_change_time() throws Exception {
		// 目录或单个文件都可以
		final String basePath = "M:\\limit\\▌Yeha▌\\Vol.249 yeha (예하) - part 01 Bad delivery guy and new wife size413";
		// true=只打印"旧值 -> 新值"不落盘; false=真正写入
		final boolean dryRun = false;
		// true=取创建时间与修改时间中较早的一个; false=写入下面的 fixedTime
		final boolean normalizeToMin = true;
		final String fixedTime = "2025-08-27 04:00:00";
		// 目录模式下需要跳过的子目录名, 不需要就传 null
		final List<String> excludeFolderList = Lists.newArrayList(
				"Vol.169 Aram"
		);

		File target = new File(basePath);
		Assume.assumeTrue("basePath 不存在, 跳过: " + basePath, target.exists());

		service.setDryRun(dryRun);
		service.setNormalizeToMin(normalizeToMin);
		service.setFixedTime(fixedTime);

		service.handleBasePath(target, excludeFolderList);
	}

	// ------------------------------------------------------------------ 入口分流

	@Test
	public void handleBasePath_not_exists_should_throw_and_change_nothing() throws Exception {
		File keepFile = temporaryFolder.newFile("keep.jpg");
		setThreeTimes(keepFile, "2020-01-02 03:04:05", "2019-06-01 04:00:00", "2021-01-01 00:00:00");
		BasicFileAttributes before = read(keepFile);

		File missing = new File(temporaryFolder.getRoot(), "no_such_folder");
		assertFalse(missing.exists());

		try {
			service.handleBasePath(missing, null);
			fail("basePath 不存在时必须抛异常");
		} catch (IllegalArgumentException expected) {
			assertTrue(expected.getMessage().contains("basePath不存在"));
		}

		assertSameTimes(before, read(keepFile));
	}

	@Test
	public void collectTargetFiles_only_images_and_skip_exclude_folder() throws Exception {
		File a = temporaryFolder.newFile("a.jpg");
		File b = temporaryFolder.newFile("b.JPEG");
		File c = temporaryFolder.newFile("c.Png");
		temporaryFolder.newFile("d.gif");
		temporaryFolder.newFile("notes.txt");
		temporaryFolder.newFile("a.jpg.bak");

		File excludeFolder = temporaryFolder.newFolder("exclude");
		new File(excludeFolder, "x.jpg").createNewFile();

		List<File> fileList = service.collectTargetFiles(temporaryFolder.getRoot(), Collections.singletonList("exclude"));

		assertEquals("只应收集到三个图片文件", 3, fileList.size());
		assertTrue(fileList.contains(a));
		assertTrue(fileList.contains(b));
		assertTrue(fileList.contains(c));
	}

	@Test
	public void collectTargetFiles_should_recurse_sub_folder() throws Exception {
		temporaryFolder.newFile("top.jpg");
		File innerFolder = temporaryFolder.newFolder("sub", "inner");
		File innerFile = new File(innerFolder, "e.jpg");
		assertTrue(innerFile.createNewFile());

		List<File> fileList = service.collectTargetFiles(temporaryFolder.getRoot(), null);

		assertEquals(2, fileList.size());
		assertTrue("子目录中的图片应当被收集", fileList.contains(innerFile));
	}

	@Test
	public void collectTargetFiles_single_file_ignores_extension() throws Exception {
		File txtFile = temporaryFolder.newFile("test.txt");
		File otherImage = temporaryFolder.newFile("other.jpg");

		List<File> fileList = service.collectTargetFiles(txtFile, null);

		assertEquals("单文件模式只返回这一个文件", 1, fileList.size());
		assertSame(txtFile, fileList.get(0));
		assertFalse("同目录其他文件不应出现", fileList.contains(otherImage));
	}

	// ------------------------------------------------------------------ 取值模式

	@Test
	public void parseFixedTime_should_be_strict() {
		FileTime fileTime = ChangeFileTimeService.parseFixedTime("2019-06-01 04:00:00");
		LocalDateTime actual = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
		assertEquals(LocalDateTime.of(2019, 6, 1, 4, 0, 0), actual);

		assertParseFail("2020-13-45 00:00:00");
		assertParseFail("2020/01/02");
		assertParseFail("2020-01-02");
		assertParseFail("");
		assertParseFail("   ");
		assertParseFail(null);
	}

	@Test
	public void normalize_uses_earlier_of_creation_and_modified() throws Exception {
		service.setDryRun(false);

		// 创建时间更晚 -> 取修改时间
		File creationLater = temporaryFolder.newFile("creation_later.jpg");
		setThreeTimes(creationLater, "2024-03-01 10:00:00", "2019-06-01 04:00:00", "2024-03-01 10:00:00");
		service.handleBasePath(creationLater, null);
		assertAligned(creationLater, "2019-06-01 04:00:00");

		// 修改时间更晚 -> 取创建时间
		File modifiedLater = temporaryFolder.newFile("modified_later.jpg");
		setThreeTimes(modifiedLater, "2019-06-01 04:00:00", "2024-03-01 10:00:00", "2024-03-01 10:00:00");
		service.handleBasePath(modifiedLater, null);
		assertAligned(modifiedLater, "2019-06-01 04:00:00");

		// 访问时间最早也应被忽略, 不能把时间拉回到 2010 年
		File accessEarliest = temporaryFolder.newFile("access_earliest.jpg");
		setThreeTimes(accessEarliest, "2020-01-01 00:00:00", "2019-06-01 04:00:00", "2010-01-01 00:00:00");
		service.handleBasePath(accessEarliest, null);
		assertAligned(accessEarliest, "2019-06-01 04:00:00");
	}

	@Test
	public void fixed_mode_requires_time_and_fails_before_touching_files() throws Exception {
		assertTrue("默认应当是对齐模式", service.isNormalizeToMin());

		File file = temporaryFolder.newFile("a.jpg");
		setThreeTimes(file, "2020-01-02 03:04:05", "2019-06-01 04:00:00", "2021-01-01 00:00:00");
		BasicFileAttributes before = read(file);

		service.setNormalizeToMin(false);

		service.setFixedTime(null);
		try {
			service.handleBasePath(file, null);
			fail("关闭对齐模式且未提供时间时必须报错");
		} catch (IllegalArgumentException expected) {
			// 期望
		}

		service.setFixedTime("   ");
		try {
			service.handleBasePath(file, null);
			fail("关闭对齐模式且时间为空白时必须报错");
		} catch (IllegalArgumentException expected) {
			// 期望
		}

		assertSameTimes(before, read(file));
	}

	// ------------------------------------------------------------------ 写入与幂等

	@Test
	public void fixed_mode_writes_all_three_times() throws Exception {
		File file = temporaryFolder.newFile("a.jpg");
		service.setNormalizeToMin(false);
		service.setFixedTime("2020-01-02 03:04:05");
		service.setDryRun(false);

		ChangeTimeResult result = service.handleBasePath(file, null);

		assertEquals(1, result.getSuccessCount());
		assertEquals(0, result.getFailCount());
		assertAligned(file, "2020-01-02 03:04:05");
	}

	@Test
	public void second_run_skips_all_and_keeps_times() throws Exception {
		File folder = temporaryFolder.newFolder("imgs");
		File first = new File(folder, "1.jpg");
		File second = new File(folder, "2.jpg");
		File third = new File(folder, "3.jpg");
		assertTrue(first.createNewFile());
		assertTrue(second.createNewFile());
		assertTrue(third.createNewFile());
		setThreeTimes(first, "2020-01-02 03:04:05", "2019-06-01 04:00:00", "2021-01-01 00:00:00");
		setThreeTimes(second, "2019-06-01 04:00:00", "2020-01-02 03:04:05", "2021-01-01 00:00:00");
		// 创建与修改本来就一致, 第一轮就该被跳过
		setThreeTimes(third, "2018-05-05 05:05:05", "2018-05-05 05:05:05", "2021-01-01 00:00:00");

		service.setDryRun(false);
		ChangeTimeResult firstResult = service.handleBasePath(folder, null);
		assertEquals("只有前两个需要修正", 2, firstResult.getSuccessCount());
		assertEquals("第三个创建与修改已一致, 应当跳过", 1, firstResult.getSkipCount());
		BasicFileAttributes afterFirstRun = read(first);

		ChangeTimeResult secondResult = service.handleBasePath(folder, null);
		assertEquals("第二次不应再有写入", 0, secondResult.getSuccessCount());
		assertEquals("第二次应当全部跳过", 3, secondResult.getSkipCount());
		assertEquals(0, secondResult.getFailCount());
		assertSameTimes(afterFirstRun, read(first));
	}

	@Test
	public void skip_when_creation_equals_modified_even_if_access_time_differs() throws Exception {
		File file = temporaryFolder.newFile("aligned.jpg");
		// 真实场景: 创建与修改已一致, 访问时间被缩略图生成刷成了当晚
		setThreeTimes(file, "2026-10-24 14:03:51", "2026-10-24 14:03:51", "2026-10-24 21:57:01");
		BasicFileAttributes before = read(file);

		service.setDryRun(false);
		ChangeTimeResult result = service.handleBasePath(file, null);

		assertEquals("创建与修改已对齐, 应当跳过", 1, result.getSkipCount());
		assertEquals("不应产生写入", 0, result.getSuccessCount());
		assertSameTimes(before, read(file));
	}

	// ------------------------------------------------------------------ 试运行与失败汇总

	@Test
	public void dry_run_is_default_and_does_not_write() throws Exception {
		assertTrue("dryRun 默认应当开启", service.isDryRun());

		File folder = temporaryFolder.newFolder("dry");
		File first = new File(folder, "1.jpg");
		File second = new File(folder, "2.jpg");
		assertTrue(first.createNewFile());
		assertTrue(second.createNewFile());
		setThreeTimes(first, "2020-01-02 03:04:05", "2019-06-01 04:00:00", "2021-01-01 00:00:00");
		setThreeTimes(second, "2019-06-01 04:00:00", "2020-01-02 03:04:05", "2021-01-01 00:00:00");
		BasicFileAttributes firstBefore = read(first);
		BasicFileAttributes secondBefore = read(second);

		ChangeTimeResult result = service.handleBasePath(folder, null);

		assertEquals(2, result.getTotalCount());
		assertEquals(0, result.getFailCount());
		assertSameTimes(firstBefore, read(first));
		assertSameTimes(secondBefore, read(second));
	}

	@Test
	public void one_failure_does_not_stop_others() throws Exception {
		final File goodFirst = temporaryFolder.newFile("good1.jpg");
		final File goodSecond = temporaryFolder.newFile("good2.jpg");
		setThreeTimes(goodFirst, "2020-01-02 03:04:05", "2019-06-01 04:00:00", "2021-01-01 00:00:00");
		setThreeTimes(goodSecond, "2020-01-02 03:04:05", "2019-06-01 04:00:00", "2021-01-01 00:00:00");
		// 收集之后文件消失, 制造一个确定性的失败
		final File vanishedFile = new File(temporaryFolder.getRoot(), "vanished.jpg");

		ChangeFileTimeService failService = new ChangeFileTimeService() {
			@Override
			public List<File> collectTargetFiles(File basePath, List<String> excludeFolderList) {
				List<File> fileList = new ArrayList<>();
				fileList.add(goodFirst);
				fileList.add(vanishedFile);
				fileList.add(goodSecond);
				return fileList;
			}
		};
		failService.setDryRun(false);

		ChangeTimeResult result = failService.handleBasePath(temporaryFolder.getRoot(), null);

		assertEquals(1, result.getFailCount());
		assertEquals(2, result.getSuccessCount());
		assertEquals(0, result.getSkipCount());
		assertAligned(goodFirst, "2019-06-01 04:00:00");
		assertAligned(goodSecond, "2019-06-01 04:00:00");
	}



	// ------------------------------------------------------------------ 测试辅助方法

	/**
	 * 设置三个时间, 参数顺序与 {@code setTimes} 一致：修改、访问、创建。
	 */
	private void setThreeTimes(File file, String creationTime, String modifiedTime, String accessTime)
			throws IOException {
		BasicFileAttributeView view = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
		view.setTimes(ChangeFileTimeService.parseFixedTime(modifiedTime),
				ChangeFileTimeService.parseFixedTime(accessTime),
				ChangeFileTimeService.parseFixedTime(creationTime));
	}

	private BasicFileAttributes read(File file) throws IOException {
		return Files.readAttributes(file.toPath(), BasicFileAttributes.class);
	}

	private void assertAligned(File file, String expectedTime) throws IOException {
		FileTime expected = ChangeFileTimeService.parseFixedTime(expectedTime);
		BasicFileAttributes attributes = read(file);
		assertEquals("创建时间不对: " + file.getName(), expected, attributes.creationTime());
		assertEquals("修改时间不对: " + file.getName(), expected, attributes.lastModifiedTime());
		assertEquals("访问时间不对: " + file.getName(), expected, attributes.lastAccessTime());
	}

	private void assertSameTimes(BasicFileAttributes before, BasicFileAttributes after) {
		assertEquals("创建时间被改动", before.creationTime(), after.creationTime());
		assertEquals("修改时间被改动", before.lastModifiedTime(), after.lastModifiedTime());
		assertEquals("访问时间被改动", before.lastAccessTime(), after.lastAccessTime());
	}

	private void assertParseFail(String timeStr) {
		try {
			ChangeFileTimeService.parseFixedTime(timeStr);
			fail("应当解析失败: " + timeStr);
		} catch (IllegalArgumentException expected) {
			// 期望
		}
	}

}
