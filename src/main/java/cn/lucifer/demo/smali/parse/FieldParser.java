package cn.lucifer.demo.smali.parse;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import cn.lucifer.util.RegexUtils;

public class FieldParser extends Parser {

	public final static HashSet<String> prefix_keywords = new HashSet<>();
	private final static String key_field = ".field";

	static {
		prefix_keywords.add(key_field);
	}

	@Override
	public ParseResultEnum parseLine(String[] words) {
		if (!prefix_keywords.contains(words[0])) {
			finished();
			return ParseResultEnum.NO_MATCH;
		}
		if (key_field.equals(words[0])) {
			words[0] = null;
			final String regex = "([\\w|$]+):L([\\w|/]+);";
			for (int i = 1, len = words.length; i < len; i++) {
				String word = words[i];
				if (Pattern.matches(regex, word)) {
					List<String> subWord = RegexUtils.getMatchChildren(word, regex);
					words[i] = changePath2Package(subWord.get(1)) + " " + subWord.get(0) + ";";
				}
			}
		}

		outLine.add(StringUtils.join(words, ' '));
		finished();
		return ParseResultEnum.CHILD_PARSE_FINISHED;
	}

}
