package cn.lucifer.demo.http.dict;

public enum CilimaoSearchTypeEnum {

	uncensored_HD("", "-uncensored-HD", "[\\d\\w]+-\\d+", "dW5jZW5zb3JlZC1IRA"),
	hdd600("hdd600.com@", "_UNCENSORED_LEAKED_NOWATERMARK.mp4", "[\\d\\w]+-\\d+", "aGRkLTYwMA"),
	;

	private CilimaoSearchTypeEnum(String prefix, String suffix, String regex, String keyword) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.regex = prefix + regex + suffix;
		this.keyword = keyword;
	}

	private final String prefix;

	private final String suffix;

	private final String regex;

	private final String keyword;

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getRegex() {
		return regex;
	}

	public String getKeyword() {
		return keyword;
	}
}
