package cn.lucifer.demo.avmoo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BtsowSearchTest {

	private final String know_uncensored = "uncensored.txt";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		BtsowSearch search = new BtsowSearch();
		boolean r = search.hasUncensored("SIRO-3180");
		System.out.println(r);
	}

	@Test
	public void testFolder() throws Exception {
//		File folder = new File("E:\\limit\\篠田ゆう");
//		File folder = new File("E:\\limit\\桃乃木かな");
//		File folder = new File("E:\\limit\\三上悠亜");
//		File folder = new File("E:\\limit\\七ツ森りり");
		File folder = new File("E:\\limit\\小島みなみ");

		final String starName = folder.getName();
		File[] subFileList = folder.listFiles();

//		List<String> knowUncensoredList = loadKnowUncensoredList(folder);
		List<String> knowUncensoredList = new ArrayList<>();

		List<String> movieNameList = new LinkedList<>();
		for (File file : subFileList) {
			if (file.isDirectory()) {
				fillMovieName(movieNameList, file, starName, knowUncensoredList);
			}
		}

		System.out.println(JSON.toJSONString(movieNameList));

		List<String> hasUncensoredList = new LinkedList<>();
		BtsowSearch search = new BtsowSearch();

		try {
			for (String movieName : movieNameList) {
				if (search.hasUncensored(movieName)) {
					hasUncensoredList.add(movieName);
				}
				Thread.sleep((long) (3000 + Math.random() * 1000));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("result = " + JSON.toJSONString(hasUncensoredList));
	}

	@Test
	public void testHdd600() throws Exception{
//		File folder = new File("E:\\limit\\篠田ゆう");
//		File folder = new File("E:\\limit\\桃乃木かな");
//		File folder = new File("E:\\limit\\三上悠亜");
//		File folder = new File("E:\\limit\\七ツ森りり");
		File folder = new File("E:\\limit\\小島みなみ");

		List<String> movieNameList =  loadKnowUncensoredList(folder);

		System.out.println(JSON.toJSONString(movieNameList));

		BtsowSearch search = new BtsowSearch();
		List<String> hasUncensoredList = new LinkedList<>();
		try {
			for (String movieName : movieNameList) {
				if (search.hasUncensored(movieName)) {
					hasUncensoredList.add(movieName);
				}
				Thread.sleep((long) (3000 + Math.random() * 1000));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("result = " + JSON.toJSONString(hasUncensoredList));
	}

	private List<String> loadKnowUncensoredList(File folder) throws IOException {
		List<String> knowUncensoredList = new ArrayList<>();
		File knowUncensoredFile = new File(folder, know_uncensored);
		if (knowUncensoredFile.exists()) {
			List<String> lines = FileUtils.readLines(knowUncensoredFile);
			for (String line : lines) {
				if (line.length() > 2) {
					knowUncensoredList.add(line.toUpperCase());
				}
			}
		}
		return knowUncensoredList;
	}

	private void fillMovieName(List<String> movieNameList, File directory, String starName, List<String> knowUncensoredList) throws IOException {
		File[] readMe = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if ("ReadMe.txt".equals(name)) {
					return true;
				}
				return false;
			}
		});
		if (ArrayUtils.isEmpty(readMe)) {
			System.err.println("缺少ReadMe.txt文件！ directory = " + directory.getAbsolutePath());
			return;
		}

		List<String> lines = FileUtils.readLines(readMe[0]);
		if (CollectionUtils.isEmpty(lines)) {
			System.err.println("ReadMe.txt文件！ 内容为空， path = " + readMe[0].getAbsolutePath());
			return;
		}

		if (lines.get(0).contains(starName)) {
			String movieName = directory.getName();
			if (knowUncensoredList.contains(movieName.toUpperCase())) {
				System.out.println("已知 uncensored = " + movieName);
				return;
			}

			movieNameList.add(movieName);
		} else {
			System.out.println("ReadMe.txt文件不包含 starName = " + starName + " , path=" + readMe[0].getAbsolutePath());
		}

	}
}