package cn.lucifer.demo.avmoo;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class BuildUncensoredReadMeTest {

	private final Set<String> skipFolderKeywordSet = Sets.newHashSet("limit");
	private final Set<String> suffixNameSet = Sets.newHashSet(".mp4", ".avi", ".wmv", ".mkv");

	@Test
	public void buildReadMe() throws Exception {
		final File targetFile = new File("E:\\limit5\\uncensored/ReadMe.txt");
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}

		final Set<String> findFolderSet = Sets.newHashSet(
				"D:\\limit3\\aaa",
				"D:\\limit3\\aaa\\limit4",
				"F:\\limit\\uncensored",
				"E:\\limit5\\uncensored"
		);


		List<String> oriLineList = FileUtils.readLines(targetFile, "utf-8");
		List<String> resultList = Lists.newArrayList(oriLineList);

		for (String findFolder : findFolderSet) {
			File folder = new File(findFolder);
			if (!folder.exists()) {
				continue;
			}

			ArrayListMultimap<File, File> subFolderMap = findSubFolder(folder);

			findVideo2ResultList(folder, null, resultList);

			for (File keyFolder : subFolderMap.keySet()) {
				List<File> subFolderList = subFolderMap.get(keyFolder);
				for (File sFolder : subFolderList) {
					findVideo2ResultList(sFolder, keyFolder, resultList);
				}
			}
		}

		// 排序
		resultList.sort(String::compareTo);
		FileUtils.writeLines(targetFile, "utf-8", resultList, "\n");
	}

	private void findVideo2ResultList(File folder, File keyFolder, List<String> resultList) {
		File[] videoList = folder.listFiles((dir, name) -> {
			for (String suffix : suffixNameSet) {
				if (name.endsWith(suffix)) {
					return true;
				}
			}
			return false;
		});

		if (null == videoList) {
			return;
		}

		for (File video : videoList) {
			// 去后缀
			String name = video.getName();
			int pos = name.lastIndexOf(".");
			name = name.substring(0, pos);

			String rName = name;
			if (null != keyFolder) {
				rName = name + "\t" + keyFolder.getName();
			}
			if (resultList.contains(rName)) {
				continue;
			}
			resultList.add(rName);
		}
	}

	private ArrayListMultimap<File, File> findSubFolder(File folder) {
		ArrayListMultimap<File, File> subFolderList = ArrayListMultimap.create();
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				if (inSkipFolder(f)) {
					continue;
				}
				subFolderList.put(f, f);
			}
		}
		if (subFolderList.isEmpty()) {
			return subFolderList;
		}

		for (File f : subFolderList.keySet()) {
			ArrayListMultimap<File, File> sMap = findSubFolder(f);
			subFolderList.putAll(f, sMap.values());
		}

		return subFolderList;
	}

	private boolean inSkipFolder(File file) {
		for (String skip : skipFolderKeywordSet) {
			if (file.getName().contains(skip)) {
				return true;
			}
		}
		return false;
	}
}
