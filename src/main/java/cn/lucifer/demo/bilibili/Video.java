package cn.lucifer.demo.bilibili;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * http://interface.bilibili.com/playurl?sign=399564451373d1ef1273185cc53e8550&cid=14788672&from=miniplay&player=1
 * 
 * @author Lucifer
 *
 */
@XStreamAlias("video")
public class Video {
	public String result;
	public long timelength;
	public String format;
	/**
	 * <![CDATA[mp4,hdmp4,flv]]>
	 */
	public String accept_format;
	/**
	 * <![CDATA[3,2,1]]>
	 */
	public String accept_quality;
	/**
	 * <![CDATA[local]]>
	 */
	public String from;
	/**
	 * <![CDATA[start]]>
	 */
	public String seek_param;
	/**
	 * <![CDATA[offset]]>
	 */
	public String seek_type;
	
	public Durl durl;
}
