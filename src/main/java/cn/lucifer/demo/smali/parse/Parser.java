package cn.lucifer.demo.smali.parse;

import java.util.LinkedList;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public abstract class Parser {
	public static Logger log = Logger.getLogger(Parser.class);

	/**
	 * 该解释器已完成使命
	 */
	public boolean isFinished;
	/**
	 * smali文件所包含的line
	 */
	protected LinkedList<String[]> smaliLinked = new LinkedList<>();
	/**
	 * 子解释器, 当有包含关系时, 则会存放在这里
	 */
	protected LinkedList<Parser> subParser = new LinkedList<>();
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
		return word.startsWith("L") && word.endsWith(";");
	}

	protected String processClassFullName(String word) {
		if (isClassFullName(word)) {
			word = StringUtils.replaceChars(word, '/', '.');
			word = word.substring(1, word.length() - 1);

			return word;
		}
		return null;
	}

	/**
	 * 输出所有的java代码行
	 * 
	 * @return
	 */
	public LinkedList<String> toLines() {
		LinkedList<String> linkedList = new LinkedList<>(outLine);
		for (Parser sub : subParser) {
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

	}
}
