package cn.lucifer.demo.avmoo;

import cn.lucifer.http.HttpSocket;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class AvmooMovie {

	private String urlStr;
	private String id;
	private String title;
	private String cover;
	private List<String> sampleImageList;
	private LinkedList<String> infoList;

	public AvmooMovie() {
	}

	public AvmooMovie(String urlStr) {
		this.urlStr = urlStr;
	}

	public void parseUrl() throws Exception {
		Document doc = AvmooStar.getDoc(urlStr);

		setterId(doc);
		setterInfoList(doc);
		setterTitle(doc);
		setterCover(doc);
		setterSampleImageList(doc);
	}

	private void setterId(Document doc) {
		Elements infoElements = doc.select("div.info p span");
		this.infoList = new LinkedList<>();
		for (Element infoElement : infoElements) {
			if (infoElement.hasClass("header")) {
				continue;
			}
			this.id = infoElement.text();
			return;
		}
	}

	private void setterInfoList(Document doc) {
		Elements infoElements = doc.select("div.info p");
		for (Element infoElement : infoElements) {
			this.infoList.add(infoElement.text());
		}
	}

	private void setterTitle(Document doc) {
		Element titleElement = doc.select("div.container h3").first();
		this.title = titleElement.text();
	}

	private void setterCover(Document doc) {
		Element coverElement = doc.select("a.bigImage").first();
		this.cover = coverElement.attr("href");
	}

	private void setterSampleImageList(Document doc) {
		Elements sampleImageList = doc.select("a.sample-box");
		this.sampleImageList = Lists.newArrayList();
		if (null == sampleImageList) {
			return;
		}
		for (Element sampleImage : sampleImageList) {
			this.sampleImageList.add(sampleImage.attr("href"));
		}
	}


	public String getUrlStr() {
		return urlStr;
	}

	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}

	public String getId() {
		return id;
	}

	public LinkedList<String> getInfoList() {
		return infoList;
	}

	public String getTitle() {
		return title;
	}

	public String getCover() {
		return cover;
	}

	public List<String> getSampleImageList() {
		return sampleImageList;
	}
}
