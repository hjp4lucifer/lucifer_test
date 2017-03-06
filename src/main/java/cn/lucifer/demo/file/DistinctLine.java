package cn.lucifer.demo.file;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * 去重复
 * 
 * @author Lucifer
 *
 */
public class DistinctLine {

	@Test
	public void test() throws IOException {
		String inPath = "D:\\workspace\\ojia_doc\\多玩饭盒改版\\小刀看到的服务器ip.txt";
		String outPath  = "results/iplist.txt";
		List<String> lines = FileUtils.readLines(new File(inPath));
		HashSet<String> set = new HashSet<>(lines);
		FileUtils.writeLines(new File(outPath), set);
	}

}
