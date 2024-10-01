package cn.lucifer.demo.file.num;

import cn.lucifer.util.StrUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FileFormatDto {
	private final String fileFormat;

	private int maxArgsCount;

	private int argIndex;
	private int maxNumLen = -1;
	private int minNumLen = Integer.MAX_VALUE;

	public FileFormatDto(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public void resetMaxMinNumLen(int len) {
		maxNumLen = Math.max(maxNumLen, len);
		minNumLen = Math.min(minNumLen, len);
	}

	public void validateMaxMinNumLen(String parentFileName) {
		if (maxNumLen <= 0) {
			String msg = StrUtils.generateStr("fn={}, maxNumLen={}小于等于0", parentFileName, maxNumLen);
			throw new RuntimeException(msg);
		}
		if (minNumLen > maxNumLen) {
			String msg = StrUtils.generateStr("fn={}, minNumLen={}比maxNumLen={}大", parentFileName, minNumLen, maxNumLen);
			throw new RuntimeException(msg);
		}
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public int getMaxArgsCount() {
		return maxArgsCount;
	}

	public void setMaxArgsCount(int maxArgsCount) {
		this.maxArgsCount = maxArgsCount;
	}

	public int getArgIndex() {
		return argIndex;
	}

	public void setArgIndex(int argIndex) {
		this.argIndex = argIndex;
	}

	public int getMaxNumLen() {
		return maxNumLen;
	}

	public int getMinNumLen() {
		return minNumLen;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		FileFormatDto that = (FileFormatDto) o;

		return new EqualsBuilder()
				.append(fileFormat, that.fileFormat)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(fileFormat)
				.toHashCode();
	}
}
