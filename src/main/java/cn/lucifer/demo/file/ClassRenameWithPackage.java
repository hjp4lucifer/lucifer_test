package cn.lucifer.demo.file;

import org.apache.commons.lang.StringUtils;

public class ClassRenameWithPackage {

	public static void main(String[] args) {
		final int arg_max_len = 4;
		if (args == null || args.length < arg_max_len) {
			System.err
					.println("need args :  [source root path] [package name] [source class simple names] [target class simple names]");
			return;
		}

		for (int i = 0; i < arg_max_len; i++) {
			if (StringUtils.trimToNull(args[i]) == null) {
				System.err.println("No need to convert Class Name !");
				return;
			}
		}

		System.out.println("------------ class rename start-----------");
		ClassRename classRename = new ClassRename();
		try {
			args = createClassRenameArgs(classRename, args);
		} catch (Exception e) {
			System.err.println("cancel class rename , because no target class name !");
			return;
		}
		classRename.init(args);
		classRename.process();
		System.out.println("------------ class rename end-----------");
	}

	protected static String[] createClassRenameArgs(ClassRename classRename,
			String[] args) throws Exception {
		String packageName = args[1];
		final String point = ".", split = ";";
		String[] oldNames = classRename.splitClassStr(args[2]);
		String[] newNames = classRename.splitClassStr(args[3]);

		StringBuffer oldNameBuffer = new StringBuffer();
		StringBuffer newNameBuffer = new StringBuffer();
		boolean start = false;
		for (int i = 0; i < newNames.length; i++) {
			oldNames[i] = StringUtils.trimToNull(classRename
					.fixString(oldNames[i]));
			newNames[i] = StringUtils.trimToNull(classRename
					.fixString(newNames[i]));
			if (newNames[i] == null || oldNames[i] == null) {
				continue;
			}

			//
			if (start) {
				oldNameBuffer.append(split);
				newNameBuffer.append(split);
			} else {
				start = true;
			}
			oldNameBuffer.append(packageName).append(point).append(oldNames[i]);
			newNameBuffer.append(packageName).append(point).append(newNames[i]);
		}

		if (!start) {
			throw new Exception("没有需要更改名字的类！");
		}
		
		return new String[] { args[0], oldNameBuffer.toString(),
				newNameBuffer.toString() };
	}
}
