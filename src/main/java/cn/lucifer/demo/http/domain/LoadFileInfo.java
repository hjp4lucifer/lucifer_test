package cn.lucifer.demo.http.domain;

public class LoadFileInfo {
	public String parentName;
	public String fileSizeGB;
	/** 展示用的修饰标记（已去掉前导下划线，多个用 / 分隔），无标记为 null */
	public String marks;
	/** 该文件允许的大小容差（GB），来源于搜索类型的配置；为 null 时使用默认容差 */
	public Double sizeTolerance;

	public LoadFileInfo(String parentName, String fileSizeGB) {
		this(parentName, fileSizeGB, null);
	}

	public LoadFileInfo(String parentName, String fileSizeGB, String marks) {
		this.parentName = parentName;
		this.fileSizeGB = fileSizeGB;
		this.marks = marks;
	}
}
