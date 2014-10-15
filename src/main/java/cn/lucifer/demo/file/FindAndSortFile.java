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
			private IntuitiveStringComparator comparator = new IntuitiveStringComparator();

			@Override
			public int compare(File o1, File o2) {
				return comparator.compare(o1.getName(), o2.getName());
			}
		});

		return files;
	}

	/**
	 * 
	 * @author Lucifer
	 *
	 */
	class SortFiles implements Comparator<File> {

		@Override
		public int compare(File o1, File o2) {
			String fn1 = o1.getName();
			fn1 = fn1.substring(0, fn1.lastIndexOf("."));
			String fn2 = o2.getName();
			fn2 = fn2.substring(0, fn2.lastIndexOf("."));
			try {
				int num1 = Integer.parseInt(fn1);
				int num2 = Integer.parseInt(fn2);
				return num1 - num2;
			} catch (Exception e) {
			}

			return fn1.compareToIgnoreCase(fn2);
		}
	}
}
