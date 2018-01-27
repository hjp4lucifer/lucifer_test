package cn.lucifer.demo.smali.parse;

public enum ParseResultEnum {
	/**
	 * 不匹配
	 */
	NO_MATCH,
	/**
	 * 继续
	 */
	GO_ON,
	/**
	 * 到子解释器
	 */
	TO_CHILD_PARSE,
	/**
	 * 子解释器使命完成
	 */
	CHILD_PARSE_FINISHED;
}
