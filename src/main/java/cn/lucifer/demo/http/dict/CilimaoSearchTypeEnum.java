package cn.lucifer.demo.http.dict;

public enum CilimaoSearchTypeEnum {

	uncensored_HD("", "-uncensored-HD", "[\\d\\w]+-\\d+", "dW5jZW5zb3JlZC1IRA", "-uncensored-nyap2p.com.mp4"),
	hdd600("hdd600.com@", "_UNCENSORED_LEAKED_NOWATERMARK.mp4", "[\\d\\w]+-\\d+", "aGRkLTYwMA"),
	;

	private CilimaoSearchTypeEnum(String prefix, String suffix, String regex, String keyword) {
		this(prefix, suffix, regex, keyword, null);
	}

	private CilimaoSearchTypeEnum(String prefix, String suffix, String regex, String keyword, String toMp4Suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.regex = prefix + regex + suffix;
		this.keyword = keyword;
		this.toMp4Suffix = toMp4Suffix;
	}

	private final String prefix;

	private final String suffix;

	private final String regex;

	private final String keyword;

	private final String toMp4Suffix;

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

	public String getToMp4Suffix() {
		return toMp4Suffix;
	}
}
