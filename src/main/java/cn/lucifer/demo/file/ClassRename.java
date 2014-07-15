package cn.lucifer.demo.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class ClassRename {

	public static void main(String[] args) {
		final int arg_max_len = 3;
		if (args == null || args.length < arg_max_len) {
			System.err
					.println("need args :  [source root path] [source class full names] [target class full names]");
			return;
		}

		for (int i = 0; i < arg_max_len; i++) {
			if (StringUtils.trimToNull(args[i]) == null) {
				System.err.println("No need to convert Class Name !");
				return;
			}
		}

		System.out.println("------------ class rename start-----------");
		new ClassRename(args).process();
		System.out.println("------------ class rename end-----------");
	}

	protected String rootPath;
	protected String[] sourceSimpleName, sourceFullName;
	protected String[] targetSimpleName, targetFullName;

	private String sourceClassNames, targetClassNames;

	/**
	 * 初始化完后需要，要先调用{@link #init(String[])}来进行初始化
	 */
	public ClassRename() {

	}

	public ClassRename(String[] args) {
		init(args);
	}

	public void init(String[] args) {
		rootPath = fixString(args[0]);
		sourceClassNames = fixString(args[1]);
		targetClassNames = fixString(args[2]);

		System.out.println("rootPath : " + rootPath);
		System.out.println("sourceClassNames : " + sourceClassNames);
		System.out.println("targetClassName : " + targetClassNames);

		setClassNameArray(sourceClassNames, targetClassNames);
		validateFiles();
	}

	protected void process() {
		try {
			rewrite();
		} catch (IOException e) {
			System.err.println("IOException !!!!");
			e.printStackTrace();
		}
	}

	private final String fileEncode = "UTF-8";

	/**
	 * 改写
	 * 
	 * @throws IOException
	 */
	protected void rewrite() throws IOException {
		File source, target;
		for (int i = 0, len = sourceFullName.length; i < len; i++) {
			source = getFile(sourceFullName[i]);
			target = getFile(targetFullName[i]);
			System.out.println("change " + sourceFullName[i] + " to "
					+ targetFullName[i]);
			fixJavaFile(source, target, i);

			source.delete();
			removeFileInAllSource(source); // 注意，当前并没有添加新文件进去

			for (File src : allSourceFiles) {
				fixJavaFile(src, src, i);
			}

			allSourceFiles.add(target);
		}
	}

	protected void fixJavaFile(File source, File target, int i)
			throws IOException {
		FileInputStream input = new FileInputStream(source);
		List<String> lines = IOUtils.readLines(input, fileEncode);
		IOUtils.closeQuietly(input);

		List<String> newLines = new ArrayList<String>(lines.size());
		boolean hasChange = false;
		boolean isSamePackage = checkSamePackage(lines, sourceFullName[i]);
		String newLine;
		for (String content : lines) {
			newLine = content;
			try {
				newLine = fixClassName(sourceFullName[i], targetFullName[i],
						newLine);
				hasChange = true;
			} catch (Exception e) {
			}
			if (hasChange || isSamePackage) {
				try {
					newLine = fixClassName(sourceSimpleName[i],
							targetSimpleName[i], newLine);
					hasChange = true;
				} catch (Exception e) {
				}
			}
			newLines.add(newLine);
		}

		if (hasChange) {
			System.out.println("  -- process: " + source.getAbsolutePath());
			// FileUtils.writeStringToFile(target, content, fileEncode);
			FileUtils.writeLines(target, fileEncode, newLines, "\r\n");
		}
	}

	protected boolean checkSamePackage(List<String> lines, String sourceFullName) {
		String packageName = getPackageName(sourceFullName);
		Pattern pattern = getPackageNamePattern(packageName);
		Matcher matcher;
		for (String line : lines) {
			matcher = pattern.matcher(line);
			if (matcher.find()) {
				//System.out.println("    == -- is same package ! ");
				return true;
			}
		}
		return false;
	}

	protected Pattern getPackageNamePattern(String packageName) {
		String regex = "^(|[ ]+)package\\W"
				+ packageName.replaceAll("\\.", "\\\\.") + "(\\W|$)";
		return Pattern.compile(regex);
	}

	protected String getPackageName(String sourceFullName) {
		return sourceFullName.substring(0, sourceFullName.lastIndexOf("."));
	}

	@Deprecated
	private void fixJavaFile_D(File source, File target, int i)
			throws FileNotFoundException, IOException {
		FileInputStream input = new FileInputStream(source);
		String content = IOUtils.toString(input, fileEncode);
		IOUtils.closeQuietly(input);

		boolean hasChange = false;
		try {
			content = fixClassName(sourceFullName[i], targetFullName[i],
					content);
			hasChange = true;
		} catch (Exception e) {
		}
		try {
			content = fixClassName(sourceSimpleName[i], targetSimpleName[i],
					content);
			hasChange = true;
		} catch (Exception e) {
		}

		if (hasChange) {
			System.out.println("  -- process: " + source.getAbsolutePath());
			FileUtils.writeStringToFile(target, content, fileEncode);
		}
	}

	protected String fixClassName(String className, String targetClassName,
			String content) throws Exception {
		Pattern pattern = getRegex(className);
		Matcher matcher = pattern.matcher(content);
		StringBuffer buffer = new StringBuffer();
		// System.out.println(matcher.groupCount());
		boolean isFind = false;
		int offset = 0, end;
		String subString;
		boolean isStringToken = false;
		while (matcher.find()) {
			isFind = true;
			end = matcher.start(1);
			subString = content.substring(offset, end);
			buffer.append(subString);
			// System.out.println(subString.indexOf("\""));
			offset = matcher.end(1);
			if (subString.indexOf("\"") != -1) {
				isStringToken = !isStringToken;
			}
			if (isStringToken) {
				buffer.append(matcher.group(1));
			} else {
				buffer.append(targetClassName);
			}
			// buffer.append(content.substring(offset, end));
			// System.out.println(end);
		}
		buffer.append(content.substring(offset, content.length()));
		if (!isFind) {
			throw new Exception("not found modify !");
		}
		return buffer.toString();
	}

	protected Pattern getRegex(String className) {
		className = StringUtils.replace(className, ".", "\\.");
		Pattern pattern = Pattern.compile("[^A-Za-z0-9_\\.](" + className
				+ ")(\\W|$)");
		return pattern;
	}

	protected void removeFileInAllSource(File src) {
		int index = 0;
		for (File f : allSourceFiles) {
			if (f.getAbsolutePath().equals(src.getAbsolutePath())) {
				allSourceFiles.remove(index);
				return;
			}
			index++;
		}
	}

	protected void validateFiles() {
		File rootFile = new File(rootPath);
		if (!rootFile.exists() || !rootFile.isDirectory()) {
			throw new RuntimeException("no such rootPath !");
		}
		validateClasses(sourceFullName, true);
		validateClasses(targetFullName, false);

		bulidSourceFiles(rootFile);
	}

	protected List<File> allSourceFiles;

	protected void bulidSourceFiles(File rootFile) {
		allSourceFiles = new ArrayList<File>(100);
		findJavaFile(rootFile);
	}

	/**
	 * 必须先初始化好{@link #allSourceFiles}
	 * 
	 * @param folder
	 */
	protected void findJavaFile(File folder) {
		File[] files = folder.listFiles(fileFilter);
		// System.out.println("files sise : " + files.length);
		for (File f : files) {
			if (f.isDirectory()) {
				findJavaFile(f);
			} else {
				allSourceFiles.add(f);
			}
		}
	}

	FileFilter fileFilter = new FileFilter() {

		@Override
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			}
			if (file.getName().endsWith(".java")) {
				return true;
			}
			return false;
		}
	};

	/**
	 * 
	 * @param classeFullNameArray
	 * @param checkExists
	 *            true检查是否存在, 不存在则报错; false检查是否不存在, 存在则报错
	 */
	protected void validateClasses(String[] classeFullNameArray,
			boolean checkExists) {
		for (String fullName : classeFullNameArray) {
			File file = getFile(fullName);
			if (checkExists) {
				if (!file.exists()) {
					throw new RuntimeException("no such java file: " + fullName
							+ ", in " + file.getAbsolutePath());
				}
			} else {
				if (file.exists()) {
					throw new RuntimeException("has exists java: " + fullName
							+ ", in " + file.getAbsolutePath());
				}
			}
		}
	}

	private File getFile(String fullName) {
		File file = new File(rootPath, fullName.replaceAll("\\.", "/")
				+ ".java");
		return file;
	}

	protected void setClassNameArray(String sourceClassNames,
			String targetClassNames) {
		sourceFullName = splitClassStr(sourceClassNames);
		sourceSimpleName = new String[sourceFullName.length];
		for (int i = 0, len = sourceFullName.length; i < len; i++) {
			sourceSimpleName[i] = getSimpleName(sourceFullName[i]);
		}

		targetFullName = splitClassStr(targetClassNames);
		targetSimpleName = new String[targetFullName.length];
		for (int i = 0, len = targetFullName.length; i < len; i++) {
			targetSimpleName[i] = getSimpleName(targetFullName[i]);
		}
	}

	protected String getSimpleName(String fullName) {
		return fullName.substring(fullName.lastIndexOf(".") + 1,
				fullName.length());
	}

	protected String[] splitClassStr(String str) {
		return str.split("(,|;)");
	}

	protected String fixString(String str) {
		return str.replaceAll("(\t| )", "");
	}

}
