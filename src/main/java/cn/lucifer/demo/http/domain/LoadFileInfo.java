package cn.lucifer.demo.http.domain;

public class LoadFileInfo {
	public String parentName;
	public String fileSizeGB;

	public LoadFileInfo(String parentName, String fileSizeGB) {
		this.parentName = parentName;
		this.fileSizeGB = fileSizeGB;
	}
}
