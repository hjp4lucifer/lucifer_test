package cn.lucifer.demo.avmoo;

import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class BtsowSearchTest {

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
		File folder = new File("D:\\limit3\\篠田ゆう");
		File[] subFileList = folder.listFiles();

		List<String> movieNameList = new LinkedList<>();
		for (File file : subFileList) {
			if (file.isDirectory()) {
				movieNameList.add(file.getName());
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
}