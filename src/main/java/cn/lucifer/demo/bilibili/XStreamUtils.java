package cn.lucifer.demo.bilibili;

import com.thoughtworks.xstream.XStream;

public class XStreamUtils {

	private static XStream xstream;
	static {
		xstream = new XStream();
		xstream.ignoreUnknownElements();
		xstream.processAnnotations(Video.class);
	}

	public static Video readVideo(String xml) {
		Video w = new Video();
		xstream.fromXML(xml, w);
		return w;
	}
}
