package cn.lucifer.demo.http.dict;

public enum CilimaoSearchTypeEnum {

	base64("", "", "", "", ".mp4"),
	uncensored_HD("", "-uncensored-HD", "[\\d\\w]+-\\d+", "dW5jZW5zb3JlZC1IRA", "-uncensored-nyap2p.com.mp4", 0.3),
	hdd600("hdd600.com@", "_UNCENSORED_LEAKED_NOWATERMARK.mp4", "[\\d\\w]+-\\d+", "aGRkLTYwMA"),
	;

	private CilimaoSearchTypeEnum(String prefix, String suffix, String regex, String keyword) {
		this(prefix, suffix, regex, keyword, null, null);
	}

	private CilimaoSearchTypeEnum(String prefix, String suffix, String regex, String keyword, String toMp4Suffix) {
		this(prefix, suffix, regex, keyword, toMp4Suffix, null);
	}

	private CilimaoSearchTypeEnum(String prefix, String suffix, String regex, String keyword, String toMp4Suffix,
								  Double sizeTolerance) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.regex = prefix + regex + suffix;
		this.keyword = keyword;
		this.toMp4Suffix = toMp4Suffix;
		this.sizeTolerance = sizeTolerance;
	}

	private final String prefix;

	private final String suffix;

	private final String regex;

	private final String keyword;

	private final String toMp4Suffix;

	/**
	 * 该类型对应的 mp4 文件允许的大小容差（GB）；为 null 时使用默认容差。
	 * 用于不同来源的压制版本，这类文件大小天然有差异
	 */
	private final Double sizeTolerance;

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

	public Double getSizeTolerance() {
		return sizeTolerance;
	}
}
