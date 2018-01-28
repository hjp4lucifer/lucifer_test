package cn.lucifer.demo.smali.parse;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import cn.lucifer.util.RegexUtils;

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
			return ParseResultEnum.GO_ON;
		}
		if (key_end.equals(words[0]) && flag_method.equals(words[1])) {
			finished();
			return ParseResultEnum.CHILD_PARSE_FINISHED;
		}

		if (key_method.equals(words[0])) {
			words[0] = null;
			if (isConstructor(words)) {
				parseConstructor(words);
			}
		}

		outLine.add(StringUtils.join(words, ' '));
		//finished();
		return ParseResultEnum.CHILD_PARSE_FINISHED;
	}

	/**
	 * <pre>
	 * 语法构成：
	 * .method public constructor <init>()V
	 * .method public constructor <init>(Ljava/lang/String;Ljava/lang/String;)V
	 * 所以重点解释最后一个word
	 * </pre>
	 * 
	 * @param words
	 */
	private void parseConstructor(String[] words) {
		int lastIndex = words.length - 1;
		// 转义前: <init>\((.*?\)(\w)
		final String regex = "<init>\\((.*?)\\)V";
		List<String> children = RegexUtils.getMatchChildren(words[lastIndex], regex);

		StrBuilder builder = new StrBuilder("(");
		// 解释出来应该size()==2
		if (StringUtils.isNotBlank(children.get(0))) {
			// 首先, 是构造方法 预分析的入参
			char[] paramsCharArray = children.get(0).toCharArray();
			LinkedList<String> params = parseParams(paramsCharArray);

			int i = 1;
			for (String p : params) {
				if (i > 1) {
					builder.append(", ");
				}
				builder.append(p).append(" p").append(i);
				i++;
			}
		}

		// 构造方法不需要考虑返回值, 所以children.get(1)无需解释
		builder.append(")");
		words[lastIndex] = builder.toString();
	}

	/**
	 * 解释参数
	 * 
	 * @param paramsCharArray
	 * @return
	 */
	private LinkedList<String> parseParams(char[] paramsCharArray) {
		LinkedList<String> params = new LinkedList<>();
		boolean inStack = false;
		for (int i = 0, len = paramsCharArray.length; i < len; i++) {
			char c = paramsCharArray[i];
			if ('[' == c) {
				inStack = true;
				outLineStack.push("[]");
				// 不触发下面公共的判断堆栈的代码块
				continue;
			} else if (KeywordDictionaryUtils.base_type_2_flag.containsKey(String.valueOf(c))) {
				params.add(KeywordDictionaryUtils.base_type_2_flag.get(String.valueOf(c)));
			} else if (KeywordDictionaryUtils.class_flag.equals(String.valueOf(c))) {
				int start = i;
				int count = 0;
				for (; i < len; i++) {
					if (KeywordDictionaryUtils.class_end_flag.equals(String.valueOf(c))) {
						break;
					}
					count++;
				}
				params.add(new String(paramsCharArray, start + 1, count - 2));
			}

			if (inStack) {
				inStack = false;
				String p = params.removeLast();
				p += outLineStack.pop();
				params.addLast(p);
			}
		}
		return params;
	}

	/**
	 * 检查是否构造函数
	 * 
	 * @param words
	 * @return
	 */
	private boolean isConstructor(String[] words) {
		for (int i = 1, len = words.length; i < len; i++) {
			String word = words[i];
			if ("constructor".equals(word)) {
				words[i] = parentParser.mainName;
				return true;
			}
		}
		return false;
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
