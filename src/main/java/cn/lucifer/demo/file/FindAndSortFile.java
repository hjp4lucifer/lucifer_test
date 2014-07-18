package cn.lucifer.demo.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;


public class FindAndSortFile {

	public File[] findBy(String folderPath, final String suffix) {
		File folder = new File(folderPath);
		File[] files = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.indexOf(suffix) > 1) {
					return true;
				}
				return false;
			}
		});

		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				String o1Name = o1.getName();
				String o2Name = o2.getName();
				return o1.getName().compareTo(o2.getName());
			}
		});

		return files;
	}

}
