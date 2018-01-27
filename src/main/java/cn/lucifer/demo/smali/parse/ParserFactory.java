package cn.lucifer.demo.smali.parse;

import org.apache.commons.lang.StringUtils;

/**
 * 解释器工厂
 * 
 * @author Lucifer
 *
 */
public class ParserFactory {

	/**
	 * 
	 * @param words
	 *            使用{@linkplain StringUtils#split(String, char)}(lineStr,
	 *            '\t')切割好的单词数组
	 * @return
	 */
	public static Parser generateParser(String[] words) {
		if (ClassParser.prefix_keywords.contains(words[0])) {
			return new ClassParser();
		}
		return null;
	}

	/**
	 * 当触发{@linkplain ParseResultEnum#GENERATE_SUB_PARSE}时才调用
	 * 
	 * @param parent
	 *            父解释器
	 * @param words
	 *            使用{@linkplain StringUtils#split(String, char)}(lineStr,
	 *            '\t')切割好的单词数组
	 * @return
	 */
	public static Parser generateChildrenParser(Parser parent, String[] words) {
		if (parent instanceof ClassParser) {
			if (FieldParser.prefix_keywords.contains(words[0])) {
				return new FieldParser();
			}
		}
		return null;
	}
}
