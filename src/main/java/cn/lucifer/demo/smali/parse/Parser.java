package cn.lucifer.demo.smali.parse;

import java.util.LinkedList;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public abstract class Parser {
	public static Logger log = Logger.getLogger(Parser.class);

	/**
	 * 主名称, 如：类名, 方法名, 字段名
	 */
	public String mainName;
	/**
	 * 该解释器已完成使命
	 */
	public boolean isFinished;
	/**
	 * 在children解释模式中
	 */
	public boolean inChildrenMode;
	/**
	 * smali文件所包含的line
	 */
	protected LinkedList<String[]> smaliLinked = new LinkedList<>();
	/**
	 * 父解释器
	 */
	public Parser parentParser;
	/**
	 * 子解释器, 当有包含关系时, 则会存放在这里
	 */
	protected LinkedList<Parser> childrenParser = new LinkedList<>();
	/**
	 * 最终输出的字符串
	 */
	protected LinkedList<String> outLine = new LinkedList<>();
	/**
	 * 最终输出的字符串堆栈
	 */
	protected Stack<String> outLineStack = new Stack<>();

	/**
	 * 解释
	 * 
	 * @param words
	 * @return
	 */
	public abstract ParseResultEnum parseLine(String[] words);

	protected boolean isClassFullName(String word) {
		return word.startsWith(KeywordDictionaryUtils.class_flag)
				&& word.endsWith(KeywordDictionaryUtils.class_end_flag);
	}

	protected String processClassFullName(String word) {
		if (isClassFullName(word)) {
			word = changePath2Package(word);
			word = word.substring(1, word.length() - 1);

			return word;
		}
		return null;
	}

	/**
	 * 把路径的string转换成package的表达方式
	 * 
	 * @param word
	 * @return
	 */
	protected String changePath2Package(String word) {
		return StringUtils.replaceChars(word, '/', '.');
	}

	/**
	 * 输出所有的java代码行
	 * 
	 * @return
	 */
	public LinkedList<String> toLines() {
		LinkedList<String> linkedList = new LinkedList<>(outLine);
		for (Parser sub : childrenParser) {
			linkedList.addAll(sub.toLines());
		}
		while (!outLineStack.empty()) {
			linkedList.add(outLineStack.pop());
		}
		return linkedList;
	}

	/**
	 * 该解释器处理完成后的动作
	 */
	public void finished() {
		isFinished = true;
	}

	public Parser getLastChildrenParser() {
		if (childrenParser.isEmpty()) {
			return null;
		}
		return childrenParser.getLast();
	}

	public void addLastChildrenParser(Parser child) {
		childrenParser.addLast(child);
	}
}
