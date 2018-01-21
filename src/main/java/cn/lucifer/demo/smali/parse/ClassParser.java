package cn.lucifer.demo.smali.parse;

import java.util.HashSet;

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
	private final static String key_class = ".class";

	static {
		prefix_keywords.add(key_class);
	}

	@Override
	public ParseResultEnum parseLine(String[] words) {
		if (!prefix_keywords.contains(words[0])) {
			finished();
			return ParseResultEnum.NO_MATCH;
		}

		if (key_class.equals(words[0])) {
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
					words[i] = className;
				}
			}
		}

		outLine.add(StringUtils.join(words, ' '));

		return ParseResultEnum.GO_ON;
	}

	@Override
	public void finished() {
		isFinished = true;
		outLine.add("{");
		outLineStack.add("}");
	}

}
