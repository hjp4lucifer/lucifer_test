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
	 * @param lineWords
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

}
