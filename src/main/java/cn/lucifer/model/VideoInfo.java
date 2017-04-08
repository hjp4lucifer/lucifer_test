package cn.lucifer.model;

import java.util.LinkedList;

public class VideoInfo {

	/**
	 * 视频地址
	 */
	public LinkedList<String> videoUrlList = new LinkedList<>();

	/**
	 * 标题
	 */
	public String title;

	/**
	 * 封面
	 */
	public String cover;
	/**
	 * 文本简介
	 */
	public String text;

	/**
	 * 时长
	 */
	public long duration;
}
