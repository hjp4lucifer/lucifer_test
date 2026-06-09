package cn.lucifer.demo.http;

import cn.lucifer.demo.http.domain.JayBotActorPageResult;
import cn.lucifer.demo.http.domain.JayBotItemInfo;
import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.NameValuePair;
import cn.lucifer.util.CookiesUtils;
import cn.lucifer.util.HttpClient5Helper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jayBot
 */
public class JayBot {
	public static final String COOKIE_DOMAIN = "45.151.132.44";

	private static final String BASE_URL = "https://45.151.132.44:2006";

	private static final String URL_TEMPLATE = "/search?wd=";

	/**
	 * 混淆脚本前缀标识
	 */
	private static final String OBFUSCATED_PREFIX = "<script>document.write(decodeURIComponent(window.atob(\"";

	private final BasicCookieStore cookieStore;

	private final String cookieToken;

	private String actorCode;

	public JayBot(BasicCookieStore cookieStore) {
		this.cookieStore = cookieStore;
		String javbot3CookieToken = CookiesUtils.getByName(cookieStore, "csrf_cookie");
		if(null == javbot3CookieToken){
			throw new IllegalArgumentException("javbot3_cookie_token is null");
		}
		this.cookieToken = javbot3CookieToken;
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

	public List<JayBotItemInfo> searchV2(String keyword) throws IOException, HttpClientException {
		String url = BASE_URL + URL_TEMPLATE + keyword;

		Document doc = getDoc(url);
		return parseDoc(doc);
	}

	protected List<JayBotItemInfo> parseDoc(Document doc) {
		Elements thumbnailList = doc.select(".thumbnail");

		List<JayBotItemInfo> resultList = Lists.newArrayListWithExpectedSize(thumbnailList.size());
		for (Element thumbnail : thumbnailList) {
			Element caption = thumbnail.select(".caption").get(0);

			JayBotItemInfo result = new JayBotItemInfo();
			Elements h3 = caption.select("h3");

			Element detailInfo = h3.select("a").get(0);
			result.detailUrl = detailInfo.attr("href");

			String title = detailInfo.text();
			String[] split = StringUtils.split(title, " ");

			result.videoNum = split[0];
			result.name = title;
			for (int i = 2; i < split.length; i++) {
				String a = split[i];
				if (a.length() >= 10) {
					continue;
				}
				if (null == result.actress) {
					result.actress = a;
				} else {
					result.actress += "，" + a;
				}
			}

			Elements pList = caption.select("p");
			for (Element p : pList) {
				String text = p.text();
				if (text.startsWith("評分：")) {
					result.score = StringUtils.substringAfter(text, "評分：");
				} else if (text.startsWith("發行日：")) {
					result.createTime = StringUtils.substringAfter(text, "發行日：");
				}
			}

			resultList.add(result);
		}
		return resultList;
	}


	public JayBotActorPageResult getByActor(String actorCode, int page) throws IOException {
		this.actorCode = actorCode;
		String url = BASE_URL + "/actor/" + actorCode + "?t=exr";

		JayBotActorPageResult result = new JayBotActorPageResult();

		if (page <= 1) {
			Document doc = getDoc(url);
			result.items = parseDoc(doc);

			result.actorName = doc.select(".panel-title").get(0).childNode(0).toString();

			result.nextPage = 1;
		} else {
			NameValuePair[] parametersBody = {new NameValuePair("stb_csrf_token", cookieToken), new NameValuePair("page", String.valueOf(page))};

			Map<String, String> header = new HashMap<>();
			header.put("referer", url);
			header.put("x-requested-with", "XMLHttpRequest");

			byte[] resp = HttpClient5Helper.httpPost(url, parametersBody, header, cookieStore);
			String str = new String(resp);
			JSONObject jsonObject = JSON.parseObject(str);
			Document doc = Jsoup.parse(jsonObject.getString("html"));
			result.items = parseDoc(doc);
			result.nextPage = jsonObject.getInteger("nextPage");
		}

		return result;
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
		byte[] resp = HttpClient5Helper.httpGet(url, null, null, cookieStore);
		String str = new String(resp, StandardCharsets.UTF_8);

		String html = decodeObfuscatedResponse(str.trim());
		return Jsoup.parse(html);
	}

	/**
	 * 如果响应是混淆脚本（document.write(decodeURIComponent(atob(...)))），
	 * 则解码出真实HTML；否则原样返回。
	 */
	private String decodeObfuscatedResponse(String raw) {
		if (raw == null || raw.isEmpty()) {
			return raw;
		}
		if (!raw.startsWith(OBFUSCATED_PREFIX)) {
			return raw;
		}
		// 从 atob(" 之后截取 Base64 字符串，到 ")));</script> 为止
		int beginIndex = OBFUSCATED_PREFIX.length();
		int endIndex = raw.indexOf("\")));</script>", beginIndex);
		if (endIndex < 0) {
			return raw;
		}
		String base64Str = raw.substring(beginIndex, endIndex);
		try {
			byte[] decodedBytes = Base64.decodeBase64(base64Str);
			return java.net.URLDecoder.decode(new String(decodedBytes, StandardCharsets.UTF_8), "UTF-8");
		} catch (Exception e) {
			// 解码失败则回退到原始响应
			return raw;
		}
	}
}
