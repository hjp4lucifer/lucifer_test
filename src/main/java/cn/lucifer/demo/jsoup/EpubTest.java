package cn.lucifer.demo.jsoup;

import java.io.File;

import org.junit.Test;

import cn.lucifer.demo.file.FindAndSortFile;

public class EpubTest {
	protected String folderPath = "E:/book/默默猴/妖刀记（1-36卷）/OPS";

	@Test
	public void testMain() {
		FindAndSortFile tools = new FindAndSortFile();
		File[] files = tools.findBy(folderPath, ".html");
		for (File file : files) {
			System.out.println(file.getName());
		}
	}
}
