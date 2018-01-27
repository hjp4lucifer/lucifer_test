package cn.lucifer.demo.smali.parse;

import java.util.HashSet;

public class MethodParser extends Parser {
	public final static HashSet<String> prefix_keywords = new HashSet<>();
	private final static String flag_method = "method";
	private final static String key_method = "." + flag_method;
	private final static String key_end = ".end";

	static {
		prefix_keywords.add(key_method);
		prefix_keywords.add(key_end);
	}

	@Override
	public ParseResultEnum parseLine(String[] words) {
		if (!prefix_keywords.contains(words[0])) {
			finished();
			return ParseResultEnum.NO_MATCH;
		}
		if (key_end.equals(words[0]) && flag_method.equals(words[1])) {
			finished();
			return ParseResultEnum.CHILD_PARSE_FINISHED;
		}
		return null;
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
