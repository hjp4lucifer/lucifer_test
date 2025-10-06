package cn.lucifer.demo.http;

import cn.lucifer.demo.http.domain.JayBotItemInfo;
import cn.lucifer.http.HttpClientException;
import cn.lucifer.util.HttpClient5Helper;
import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jayBot
 */
public class JayBot {
	private static final String BASE_URL = "https://javbot3.top";

	private static final String URL_TEMPLATE = "/search?wd=";

	private final String cookie;

	private final Map<String, String> header = new HashMap<>();

	public JayBot(String cookie) {
		this.cookie = cookie;
		// 基本请求头设置
		header.put("cookie", cookie);
	}

	public List<String> search(String keyword) throws IOException, HttpClientException {
		String url = BASE_URL + URL_TEMPLATE + keyword;

		Document doc = getDoc(url);
		Elements itemImgList = doc.select(".item-img");

		List<String> resultList = Lists.newArrayListWithExpectedSize(itemImgList.size());
		for (Element itemImg : itemImgList) {
			String detailUrl = itemImg.attr("href");
			resultList.add(detailUrl);
		}
		return resultList;
	}

	public JayBotItemInfo getDetail(String detailUrl) throws IOException {
		String url = BASE_URL + detailUrl;
		Document doc = getDoc(url);

		JayBotItemInfo result = new JayBotItemInfo();
		result.name = doc.select(".topic-title").get(0).text();

		Element movie_des = doc.select(".movie-des").get(0);

		Element col_md_4 = movie_des.select(".col-md-4").get(0);
		Elements infoList = col_md_4.select("p");

		for (Element info : infoList) {
			String text = info.text();
			if (text.startsWith("番號：")) {
				result.videoNum = StringUtils.substringAfter(text, "番號：");
			} else if (text.startsWith("發行日：")) {
				result.createTime = StringUtils.substringAfter(text, "發行日：");
			} else if (text.startsWith("時長：")) {
				result.videoTime = StringUtils.substringAfter(text, "時長：");
			} else if (text.startsWith("演員：")) {
				result.actress = StringUtils.substringAfter(text, "演員：");
			} else if (text.startsWith("請評分（")) {
				result.score = info.select("#rating").get(0).attr("value");
				result.ratingNum = info.select("#rating-mum").get(0).text();
			}
		}


		return result;
	}

	private Document getDoc(String url) throws IOException, HttpClientException {
		byte[] resp = HttpClient5Helper.httpGet(url, null, header);
		String str = new String(resp);
		return Jsoup.parse(str);
	}
}
