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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * {@link FolderRecentCheckService} 的测试类。<br>
 * <br>
 * 所有用例都通过 {@code setReferenceDate} 固定"今天"，避免结果随真实日期漂移。<br>
 * 参考日期 {@code 2026-10-25}、月数 6 时，截止日为 {@code 2026-04-25}：文件的日期必须<b>晚于</b>它才算命中。
 */
public class FolderRecentCheckServiceTest {

	/** 用例里统一的参考日期 */
	private static final LocalDate TODAY = LocalDate.of(2026, 10, 25);

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private FolderRecentCheckService service;

	@Before
	public void setUp() throws Exception {
		service = new FolderRecentCheckService();
		service.setReferenceDate(TODAY);
	}

	// ------------------------------------------------------------------ 手动执行入口

	/**
	 * 手动执行入口：改下面几个参数, 然后直接运行本方法即可。<br>
	 * 只读操作, 不会修改任何文件；路径不存在时自动跳过, 不会让整个测试类报错。
	 */
	@Test
	public void Yeha_recent_check() throws Exception {
		// 指向一个目录（逐层判定每个含图片的子目录）或单个文件
		final String basePath = "M:\\limit\\▌Yeha▌";
		// 找出最近多少个月内有图片更新的目录
		final int recentMonths = 6;
		// 需要跳过的子目录名, 不需要就传 null
		final List<String> excludeFolderList = Lists.newArrayList(
				"Vol.169 Aram",
				"Vol.229 Yeha"
		);

		File target = new File(basePath);
		Assume.assumeTrue("basePath 不存在, 跳过: " + basePath, target.exists());

		service.setRecentMonths(recentMonths);
		service.check(target, excludeFolderList);
	}

	// ------------------------------------------------------------------ 命中规则

	@Test
	public void any_recent_image_makes_folder_hit() throws Exception {
		File folder = temporaryFolder.newFolder("vol");
		createFile(folder, "old.jpg", "2020-01-01 10:00:00", "2020-01-01 10:00:00");
		File hitFile = createFile(folder, "new.jpg", "2026-09-01 10:00:00", "2026-09-01 10:00:00");

		service.setRecentMonths(6);
		List<FolderRecentReport> reportList = service.check(folder, null);

		assertEquals("只要有一张新图, 整个目录就命中", 1, reportList.size());
		FolderRecentReport report = reportList.get(0);
		assertEquals(hitFile.getName(), report.getHitFile().getName());
		assertEquals(LocalDate.of(2026, 9, 1), report.getHitDate());
		assertEquals(folder.getAbsolutePath(), report.getTarget().getAbsolutePath());
	}

	@Test
	public void all_images_older_than_cutoff_should_not_hit() throws Exception {
		File folder = temporaryFolder.newFolder("old");
		createFile(folder, "a.jpg", "2020-01-01 10:00:00", "2020-01-01 10:00:00");
		createFile(folder, "b.jpg", "2021-05-01 10:00:00", "2021-05-01 10:00:00");

		service.setRecentMonths(6);
		assertEquals("全部早于截止日, 不命中", 0, service.check(folder, null).size());
	}

	@Test
	public void hit_exactly_on_cutoff_should_not_count() throws Exception {
		File folder = temporaryFolder.newFolder("cutoff");
		File file = new File(folder, "a.jpg");
		assertTrue(file.createNewFile());
		service.setRecentMonths(6);

		// 截止日 = 2026-10-25 - 6 个月 = 2026-04-25, 要求"大于"才算命中
		setTimes(file, "2026-04-25 10:00:00", "2026-04-25 10:00:00", "2026-04-25 10:00:00");
		assertEquals("恰好等于截止日不命中", 0, service.check(folder, null).size());

		setTimes(file, "2026-04-26 10:00:00", "2026-04-26 10:00:00", "2026-04-26 10:00:00");
		assertEquals("晚一天就命中", 1, service.check(folder, null).size());
	}

	@Test
	public void access_time_earlier_should_be_ignored() throws Exception {
		File folder = temporaryFolder.newFolder("atime");
		File file = new File(folder, "a.jpg");
		assertTrue(file.createNewFile());
		// 创建与修改都是 2026-09-01, 只有访问时间是 2015
		setTimes(file, "2026-09-01 10:00:00", "2026-09-01 10:00:00", "2015-01-01 00:00:00");

		service.setRecentMonths(6);
		List<FolderRecentReport> reportList = service.check(folder, null);

		assertEquals("访问时间不参与计算, 该目录仍命中", 1, reportList.size());
		assertEquals(LocalDate.of(2026, 9, 1), reportList.get(0).getHitDate());
	}

	@Test
	public void file_time_is_earlier_of_creation_and_modified() throws Exception {
		File folder = temporaryFolder.newFolder("minOfTwo");
		// 修改时间很新, 但创建时间是 2020 -> 取 2020, 不命中
		File creationOld = new File(folder, "creation_old.jpg");
		assertTrue(creationOld.createNewFile());
		setTimes(creationOld, "2020-01-01 10:00:00", "2026-09-01 10:00:00", "2026-09-01 10:00:00");

		service.setRecentMonths(6);
		assertEquals("创建时间更早时应取创建时间", 0, service.check(folder, null).size());

		// 反过来: 创建时间新, 修改时间是 2020 -> 也取 2020
		setTimes(creationOld, "2026-09-01 10:00:00", "2020-01-01 10:00:00", "2026-09-01 10:00:00");
		assertEquals("修改时间更早时应取修改时间", 0, service.check(folder, null).size());
	}

	@Test
	public void single_file_entry_checks_only_that_file() throws Exception {
		File txtFile = temporaryFolder.newFile("test.txt");
		setTimes(txtFile, "2026-08-01 10:00:00", "2026-08-01 10:00:00", "2026-08-01 10:00:00");

		service.setRecentMonths(6);
		List<FolderRecentReport> reportList = service.check(txtFile, null);

		assertEquals("单文件入口不筛选扩展名, 应产生一条命中", 1, reportList.size());
		FolderRecentReport report = reportList.get(0);
		assertEquals(txtFile.getAbsolutePath(), report.getTarget().getAbsolutePath());
		assertEquals(txtFile.getName(), report.getHitFile().getName());
	}

	@Test
	public void default_months_and_parameter_validation() throws Exception {
		assertEquals("默认范围应为 6 个月", FolderRecentCheckService.DEFAULT_RECENT_MONTHS, service.getRecentMonths());

		File folder = temporaryFolder.newFolder("defaultMonths");
		createFile(folder, "a.jpg", "2026-06-02 10:00:00", "2026-06-02 10:00:00");

		assertEquals(1, service.check(folder, null).size());
		service.setRecentMonths(6);
		assertEquals("显式传 6 应与默认值一致", 1, service.check(folder, null).size());

		// 1 个月: 截止日 2026-09-25, 2026-06-02 已经早于它
		service.setRecentMonths(1);
		assertEquals(0, service.check(folder, null).size());

		service.setRecentMonths(0);
		try {
			service.check(folder, null);
			fail("recentMonths 为 0 时必须报错");
		} catch (IllegalArgumentException expected) {
			// 期望
		}

		service.setRecentMonths(-1);
		try {
			service.check(folder, null);
			fail("recentMonths 为负数时必须报错");
		} catch (IllegalArgumentException expected) {
			// 期望
		}
	}

	// ------------------------------------------------------------------ 逐层判定与排除

	@Test
	public void container_folder_should_not_match() throws Exception {
		File root = temporaryFolder.newFolder("root");
		File subFolder = temporaryFolder.newFolder("root", "sub1");
		createFile(subFolder, "recent.jpg", "2026-09-01 10:00:00", "2026-09-01 10:00:00");

		service.setRecentMonths(6);
		List<FolderRecentReport> reportList = service.check(root, null);

		assertEquals(1, reportList.size());
		assertEquals("命中应落在子目录上", subFolder.getAbsolutePath(),
				reportList.get(0).getTarget().getAbsolutePath());
	}

	@Test
	public void parent_and_sub_folder_are_judged_independently() throws Exception {
		File root = temporaryFolder.newFolder("mixed");
		createFile(root, "old.jpg", "2020-01-01 10:00:00", "2020-01-01 10:00:00");
		File subFolder = temporaryFolder.newFolder("mixed", "sub");
		createFile(subFolder, "recent.jpg", "2026-09-01 10:00:00", "2026-09-01 10:00:00");

		service.setRecentMonths(6);
		List<FolderRecentReport> reportList = service.check(root, null);

		assertEquals("父目录自己只有旧图, 不应因为子目录而命中", 1, reportList.size());
		assertEquals(subFolder.getAbsolutePath(), reportList.get(0).getTarget().getAbsolutePath());
	}

	@Test
	public void exclude_folder_should_skip_whole_sub_tree() throws Exception {
		File root = temporaryFolder.newFolder("exclude");
		File excludeFolder = temporaryFolder.newFolder("exclude", "Vol.169 Aram");
		createFile(excludeFolder, "recent.jpg", "2026-09-01 10:00:00", "2026-09-01 10:00:00");
		File excludeSubFolder = temporaryFolder.newFolder("exclude", "Vol.169 Aram", "nested");
		createFile(excludeSubFolder, "nested_recent.jpg", "2026-09-05 10:00:00", "2026-09-05 10:00:00");

		File normalFolder = temporaryFolder.newFolder("exclude", "Vol.300");
		createFile(normalFolder, "recent.jpg", "2026-10-20 10:00:00", "2026-10-20 10:00:00");

		service.setRecentMonths(6);
		List<FolderRecentReport> reportList = service.check(root, Collections.singletonList("Vol.169 Aram"));

		assertEquals("被排除目录及其子树都不应出现", 1, reportList.size());
		assertEquals(normalFolder.getAbsolutePath(), reportList.get(0).getTarget().getAbsolutePath());
	}

	// ------------------------------------------------------------------ 输出与只读

	@Test
	public void report_should_be_sorted_by_path_and_complete() throws Exception {
		File root = temporaryFolder.newFolder("sorted");
		File third = temporaryFolder.newFolder("sorted", "d3");
		createFile(third, "a.jpg", "2026-09-15 10:00:00", "2026-09-15 10:00:00");
		File first = temporaryFolder.newFolder("sorted", "d1");
		createFile(first, "a.jpg", "2026-08-01 10:00:00", "2026-08-01 10:00:00");
		File second = temporaryFolder.newFolder("sorted", "d2");
		createFile(second, "a.jpg", "2026-10-01 10:00:00", "2026-10-01 10:00:00");

		service.setRecentMonths(6);
		List<FolderRecentReport> reportList = service.check(root, null);

		assertEquals(3, reportList.size());
		assertTrue(reportList.get(0).getTarget().getAbsolutePath().endsWith("d1"));
		assertTrue(reportList.get(1).getTarget().getAbsolutePath().endsWith("d2"));
		assertTrue(reportList.get(2).getTarget().getAbsolutePath().endsWith("d3"));

		for (FolderRecentReport report : reportList) {
			assertNotNull(report.getTarget());
			assertNotNull("每条命中都要能定位到文件", report.getHitFile());
			assertNotNull(report.getHitDate());
			assertTrue(report.toString().contains(report.getTarget().getAbsolutePath()));
		}
	}

	@Test
	public void check_should_not_modify_any_file() throws Exception {
		File folder = temporaryFolder.newFolder("readOnly");
		File first = createFile(folder, "a.jpg", "2020-01-01 10:00:00", "2019-06-01 10:00:00");
		File second = createFile(folder, "b.jpg", "2026-10-20 10:00:00", "2026-10-20 10:00:00");
		BasicFileAttributes firstBefore = read(first);
		BasicFileAttributes secondBefore = read(second);

		service.setRecentMonths(6);
		service.check(folder, null);

		assertSameTimes(firstBefore, read(first));
		assertSameTimes(secondBefore, read(second));
	}


	// ------------------------------------------------------------------ 测试辅助方法

	private File createFile(File folder, String fileName, String creationTime, String modifiedTime)
			throws IOException {
		File file = new File(folder, fileName);
		assertTrue("创建文件失败: " + file.getAbsolutePath(), file.createNewFile());
		setTimes(file, creationTime, modifiedTime, modifiedTime);
		return file;
	}

	/**
	 * 设置三个时间, 参数顺序与 {@code setTimes} 一致：修改、访问、创建。
	 */
	private void setTimes(File file, String creationTime, String modifiedTime, String accessTime)
			throws IOException {
		BasicFileAttributeView view = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
		FileTime creation = ChangeFileTimeService.parseFixedTime(creationTime);
		FileTime modified = ChangeFileTimeService.parseFixedTime(modifiedTime);
		FileTime access = ChangeFileTimeService.parseFixedTime(accessTime);
		view.setTimes(modified, access, creation);
	}

	private BasicFileAttributes read(File file) throws IOException {
		return Files.readAttributes(file.toPath(), BasicFileAttributes.class);
	}

	private void assertSameTimes(BasicFileAttributes before, BasicFileAttributes after) {
		assertEquals("创建时间被改动", before.creationTime(), after.creationTime());
		assertEquals("修改时间被改动", before.lastModifiedTime(), after.lastModifiedTime());
		assertEquals("访问时间被改动", before.lastAccessTime(), after.lastAccessTime());
	}

}
