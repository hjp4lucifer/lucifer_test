package cn.lucifer.demo.bilibili;

import java.net.URL;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("durl")
public class Durl {

	public String order;
	public long length;
	public long size;
	public String url;
	public List<URL> backup_url;
}
