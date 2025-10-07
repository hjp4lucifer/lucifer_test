package cn.lucifer.demo.file;

import cn.lucifer.util.StrUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.*;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class LimitVideoToolsTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	final ImmutableSet<String> NO_CHECK_FOLDER_SET = ImmutableSet.<String>builder().add("images", "cover").build();

	@Test
	public void mp4_to_txt() throws Exception {
		String destFn = "明里つむぎ_mark_UR";
		File folder = new File("S:\\limit\\UR\\" + destFn);
		File outFolder = new File("F:\\limit\\" + destFn);
		assertTrue(folder.exists());

		File[] mp4FileList = folder.listFiles(f -> {
					String fn = f.getName();
					if (fn.contains("-uncensored-")) {
						return false;
					}
					if (fn.endsWith("-AI.mp4")) {
						return false;
					}
					return fn.endsWith(".mp4");
				}
		);
		for (File mp4File : mp4FileList) {
			File txtFile = new File(outFolder, StringUtils.replace(mp4File.getName(), ".mp4", ".txt"));
			if (txtFile.exists()) {
				continue;
			}
			txtFile.createNewFile();
		}
	}

	@Test
	public void all_task() throws Exception {
		buildIndexFile_girlName();
		buildIndexFile_limitMp4();
	}

	@Test
	public void buildIndexFile_girlName() throws Exception {
		final File folder = new File("S:\\limit");
		final FileInputStream blacklistFile = new FileInputStream("M:\\limit\\aaa\\blacklist_G.txt");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		final String outFn = StrUtils.generateMessage("M:\\limit\\aaa\\limit_girl_{}.txt", dateFormat.format(new Date()));

		List<File> subFolderList = findSubFolderList(folder);
		ArrayListMultimap<String, File> girlNameMap = ArrayListMultimap.create();


		List<String> blackListLine = IOUtils.readLines(blacklistFile, "utf-8");
		IOUtils.close(blacklistFile);

		List<String> outLineList = Lists.newArrayListWithExpectedSize(blackListLine.size() + subFolderList.size());

		for (String line : blackListLine) {
			outLineList.add(StrUtils.generateMessage("{}\t{}", line, "blacklist"));
		}

		for (File f : subFolderList) {
			String fn = f.getName();
			String girlName = StringUtils.substringBefore(fn, "_");

			boolean repeat = girlNameMap.containsKey(girlName);

			if (repeat) {
				String msg = StrUtils.generateMessage("{}\t*****  repeat  *****\tsource={}, dest={}",
						girlName,
						f.getAbsolutePath(),
						girlNameMap.get(girlName).get(0).getAbsolutePath());
				outLineList.add(msg);
			} else {
				girlNameMap.put(girlName, f);
				outLineList.add(StrUtils.generateMessage("{}\t{}", girlName, f.getParentFile().getName()));
			}
		}

		FileUtils.writeLines(new File(outFn), "utf-8", outLineList);
	}

	@Test
	public void buildIndexFile_limitMp4() throws Exception {
		final File folder = new File("S:\\limit");
		final FileInputStream blacklistFile = new FileInputStream("S:\\limit\\blacklist_V.txt");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		final String outFn = StrUtils.generateMessage("M:\\limit\\aaa\\limit_mp4_{}.txt", dateFormat.format(new Date()));

		final ImmutableSet<String> fileSuffix = ImmutableSet.<String>builder()
				.add(".mp4").add("wmv").add("avi").add(".mkv").add(".blacklist").build();

		List<File> subFolderList = findSubFolderList(folder);
		ArrayListMultimap<String, File> mp4NameMap = ArrayListMultimap.create();

		List<String> blackListLine = IOUtils.readLines(blacklistFile, "utf-8");
		IOUtils.close(blacklistFile);

		List<String> outLineList = Lists.newArrayListWithExpectedSize(blackListLine.size() + subFolderList.size());
		for (String line : blackListLine) {
			outLineList.add(StrUtils.generateMessage("{}\t{}", line, "blacklist"));
		}


		for (File subFolder : subFolderList) {
			String girlName = StringUtils.substringBefore(subFolder.getName(), "_");

			File[] mp4List = subFolder.listFiles(f -> {
				String fn = f.getName();
				for (String suffix : fileSuffix) {
					if (fn.endsWith(suffix)) {
						return true;
					}
				}
				return false;
			});
			if (ArrayUtils.isEmpty(mp4List)) {
				continue;
			}
			for (File f : mp4List) {
				String mp4 = f.getName();
				boolean repeat = mp4NameMap.containsKey(mp4);

				if (repeat) {
					String msg = StrUtils.generateMessage("{}\t*****  repeat  *****\tsource={}, dest={}",
							mp4,
							f.getAbsolutePath(),
							mp4NameMap.get(mp4).get(0).getAbsolutePath());
					outLineList.add(msg);
				} else {
					mp4NameMap.put(mp4, f);
					outLineList.add(StrUtils.generateMessage("{}\t{}", mp4, girlName));
				}
			}
		}
		FileUtils.writeLines(new File(outFn), "utf-8", outLineList);
	}

	private List<File> findSubFolderList(File folder) {
		File[] subList = folder.listFiles(f -> {
			if (NO_CHECK_FOLDER_SET.contains(f.getName())) {
				return false;
			}
			return f.isDirectory();
		});
		if (ArrayUtils.isEmpty(subList)) {
			return Lists.newArrayList();
		}

		List<File> resultList = Lists.newArrayList(subList);
		for (File file : subList) {
			resultList.addAll(findSubFolderList(file));
		}

		return resultList;
	}

	@Test
	public void findSame() throws Exception {
		final File folder = new File("F:\\limit\\篠田ゆう_mark_MR");
		if (!folder.exists()) {
			throw new RuntimeException("folder not exists");
		}

		ArrayListMultimap<String, String> mp4NameMap = ArrayListMultimap.create();
		File[] listFiles = folder.listFiles();
		for (File f : listFiles) {
			String name = f.getName();
			name = StringUtils.remove(name, "hhd800.com@");
			name = StringUtils.remove(name, ".mp4");
			// 截取前2位
			String[] splitArray = StringUtils.split(name, '-');
			mp4NameMap.put(splitArray[0] + "-" + splitArray[1], f.getName());
		}

		// 查找重复的文件
		Map<String, List<String>> resultMap = Maps.newHashMapWithExpectedSize(mp4NameMap.keySet().size());
		for (String key : mp4NameMap.keySet()) {
			List<String> valueList = mp4NameMap.get(key);
			if (valueList.size() > 1) {
				resultMap.put(key, valueList);
			}
		}
		logger.info("result={}", JSON.toJSONString(resultMap));
	}
}
