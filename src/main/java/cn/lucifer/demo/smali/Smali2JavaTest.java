package cn.lucifer.demo.smali;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.lucifer.demo.smali.parse.ParseResultEnum;
import cn.lucifer.demo.smali.parse.Parser;
import cn.lucifer.demo.smali.parse.ParserFactory;

public class Smali2JavaTest {

	public static Logger log = Logger.getLogger(Smali2JavaTest.class);

	private final Stack<Parser> keyStack = new Stack<>();
	private final String smaliPath = "resource/smali/a.smali";
	private final LinkedList<Parser> linked = new LinkedList<>();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws IOException {
		List<String> lines = FileUtils.readLines(new File(smaliPath));
		for (String line : lines) {
			parseLine(line);
		}

		// 汇总结果
		LinkedList<String> outLines = new LinkedList<>();
		for (Parser parser : linked) {
			outLines.addAll(parser.toLines());
		}

		for (String line : outLines) {
			System.out.println(line);
		}
	}

	/**
	 * 解释line的思路
	 * <ol>
	 * <li>调用{@link #linked}的getLast()方法, 获取正在使用的Parser(排除finished的)</li>
	 * <li>未完成的, 继续添加到{@link Parser#smaliLinked}里面去<br>
	 * 已完成的, 通过解释器工厂, 获取对应的解释器</li>
	 * <li>然后调用{@link Parser#parseLine(String[])}(假如返回
	 * {@linkplain ParseResultEnum#NO_MATCH}, 则再次调用工厂)</li>
	 * <li></li>
	 * </ol>
	 * 
	 * @param line
	 */
	private void parseLine(String line) {
		if (StringUtils.isBlank(line)) {
			return;
		}

		String[] words = StringUtils.split(line, ' ');

		Parser parser = null;
		if (!linked.isEmpty()) {
			parser = linked.getLast();
		}

		if (null == parser || parser.isFinished) {
			parser = ParserFactory.generateParser(words);
			if (null == parser) {
				return;
			}
			linked.add(parser);
		}

		ParseResultEnum r = parser.parseLine(words);
		if (ParseResultEnum.NO_MATCH == r) {
			parser = ParserFactory.generateParser(words);
			if (null == parser) {
				return;
			}
			linked.add(parser);
			r = parser.parseLine(words);
		}

	}

}
