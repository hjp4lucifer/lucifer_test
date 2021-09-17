package cn.lucifer.demo.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Set;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class FileReName {

	@Test
	public void testMain() {
		String pathname = "E:/books/chenyuan";
		File folder = new File(pathname);
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					String name = file.getName();
					if (name.equals("000序章 相约.doc")
							|| name.equals("405终章 一曲千年.doc")) {
						continue;
					}
					if (name.indexOf("章") > 0) {
						String deleteString = name.substring(name.indexOf("章"),
								name.lastIndexOf(" ") + 1);
						// System.out.print("delete : " + deleteString + "  ");
						// System.out.print("old : " + name + "  ");
						name = name.replace(deleteString, "");
						// System.out.println(name);
						File renameFile = new File(pathname + "/" + name);
						file.renameTo(renameFile);
					}
				}
			}
		}
		System.out.println("ok!");
	}

	@Test
	public void testSome() {
		final String path = "E:\\迅雷下载\\【红馆hgfby.me hgfby.vip】自购分享46个微博福利姬合集[13000+P100+V 12.1G ]\\微博主压缩\\黑暗萝莉全套+视频\\黑暗萝莉\\全套图包";

		Set<Integer> skipSet = Sets.newHashSet();

		File folder = new File(path);
		for (int i = 1; i <= 364; i++) {
			if (skipSet.contains(i)) {
				continue;
			}
			String fn = String.format("【红馆hgfby.me hgfby.vip】 (%d).jpg.bt.xltd", i);
			File f = new File(folder, fn);
			if (f.exists()) {
				f.renameTo(new File(folder, StringUtils.removeEnd(fn, ".bt.xltd")));
			}
		}
	}

	@Test
	public void testRemove_xltd() {
		final String path = "E:\\limit5\\极品高颜值【AM女神】完美露脸大尺度啪啪私拍流出 极品丰臀骑乘做爱 抖音诱惑 高清私拍152P 高清1080P原版无水印\\推特博主【可儿Baby 】大尺度性爱私拍流出 骑乘啪啪顶着操浪叫 高清私拍51P\\51P";

		Set<Integer> skipSet = Sets.newHashSet();

		File folder = new File(path);
		File[] fileList = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".bt.xltd");
			}
		});
		for (File file : fileList) {
			String fn = file.getName();
			fn = StringUtils.removeEnd(fn, ".bt.xltd");
			file.renameTo(new File(folder, fn));
		}
	}
}
