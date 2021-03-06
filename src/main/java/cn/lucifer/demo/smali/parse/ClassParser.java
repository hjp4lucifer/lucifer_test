package cn.lucifer.demo.smali.parse;

import java.util.HashSet;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

/**
 * class解释器
 * 
 * @author Lucifer
 *
 */
public class ClassParser extends Parser {

	public final static HashSet<String> prefix_keywords = new HashSet<>();
	public final static HashSet<String> class_prefix_keywords = new HashSet<>();
	public final static HashSet<String> sub_prefix_keywords = new HashSet<>();
	private final static String key_class = ".class";
	private final static String key_super = ".super";
	private final static String key_implements = ".implements";
	private LinkedList<String> key_list = new LinkedList<>();

	static {
		class_prefix_keywords.add(key_class);
		class_prefix_keywords.add(key_super);
		class_prefix_keywords.add(key_implements);

		sub_prefix_keywords.addAll(FieldParser.prefix_keywords);
		sub_prefix_keywords.addAll(MethodParser.prefix_keywords);

		prefix_keywords.addAll(class_prefix_keywords);
		prefix_keywords.addAll(sub_prefix_keywords);
	}

	@Override
	public ParseResultEnum parseLine(String[] words) {
		if (!prefix_keywords.contains(words[0])) {
			finished();
			return ParseResultEnum.NO_MATCH;
		}
		if (sub_prefix_keywords.contains(words[0])) {
			if (!inChildrenMode) {
				finished();
			}
			isFinished = false;
			inChildrenMode = true;
			return ParseResultEnum.TO_CHILD_PARSE;
		}

		if (key_class.equals(words[0])) {
			key_list.add(key_class);
			words[0] = null;
			for (int i = 1, len = words.length; i < len; i++) {
				String word = words[i];
				if (StringUtils.isBlank(word)) {
					continue;
				}
				if (isClassFullName(word)) {
					word = processClassFullName(words[i]);
					int point = word.lastIndexOf(".");
					String line = new StrBuilder("package ").append(word.substring(0, point)).append(";").toString();
					outLine.add(line);

					String className = word.substring(point + 1);
					words[i] = "class " + className;
					mainName = className;
				}
			}
		} else if (key_super.equals(words[0])) {
			key_list.add(key_super);
			words[0] = " extends ";
			simpleChange(words);
		} else if (key_implements.equals(words[0])) {
			if (key_implements.equals(key_list.getLast())) {
				words[0] = " , ";
			} else {
				key_list.add(key_implements);
				words[0] = " implements ";
			}
			simpleChange(words);
		}

		outLine.add(StringUtils.join(words, ' '));

		return ParseResultEnum.GO_ON;
	}

	/**
	 * 简单转换, 就是把类名转换出来
	 * 
	 * @param words
	 */
	private void simpleChange(String[] words) {
		for (int i = 1, len = words.length; i < len; i++) {
			String word = words[i];
			if (StringUtils.isBlank(word)) {
				continue;
			}
			if (isClassFullName(word)) {
				words[i] = processClassFullName(words[i]);
			}
		}
	}

	@Override
	public void finished() {
		if (!inChildrenMode) {
			outLine.add("{");
			outLineStack.add("}");
		}
		isFinished = true;
		inChildrenMode = false;
	}

}
