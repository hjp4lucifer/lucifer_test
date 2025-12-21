package cn.lucifer.demo.http;

import cn.lucifer.demo.http.dict.CilimaoSearchTypeEnum;
import cn.lucifer.demo.http.domain.CilimaoLinkedInfo;
import cn.lucifer.http.HttpClientException;
import cn.lucifer.util.HttpClient5Helper;
import cn.lucifer.util.StrUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 磁力猫
 * cilimao.app
 */
public class CilimaoApp {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String BASE_URL = "https://clm52.top/";
	private final String url_template;

	// 定义正则表达式
	private final String regex;

	// 编译Pattern对象
	private final Pattern pattern;

	private final BasicCookieStore cookieStore;

	public CilimaoApp(CilimaoSearchTypeEnum searchType, BasicCookieStore cookieStore) {
		this.cookieStore = cookieStore;
		this.regex = searchType.getRegex();
		this.pattern = Pattern.compile(regex);
		this.url_template = StrUtils.generateMessage("search?word={}&sort=time&p=", searchType.getKeyword());
	}

	public List<CilimaoLinkedInfo> getLinkedInfoList(int page) throws IOException, HttpClientException {
		String urlStr = BASE_URL + url_template + page;
		logger.info("load url={}", urlStr);

		Document doc = getDoc(urlStr);
		Elements search_list_wrapper = doc.select("#Search_list_wrapper");
		Elements liArray = search_list_wrapper.get(0).children();

		List<CilimaoLinkedInfo> resultList = Lists.newArrayListWithExpectedSize(liArray.size());

		for (Element li : liArray) {
			if (!"li".equals(li.nodeName())) {
				continue;
			}
			Element a = li.select(".SearchListTitle_result_title").get(0);
			Element info = li.select(".Search_list_info").get(0);
			String createTime = StringUtils.substringBetween(info.text(), "创建时间：", "文件格式：");

			CilimaoLinkedInfo r = new CilimaoLinkedInfo();
			r.name = a.text();
			r.url = BASE_URL + a.attr("href");
			r.createTime = createTime;

			logger.info("infoDto={}", JSON.toJSONString(r));


			// 创建Matcher对象
			Matcher matcher = pattern.matcher(r.name);
			if (!matcher.matches()) {
				logger.warn("不符合定义的文件格式! fileName={}", r.name);
				continue;
			}

			resultList.add(r);
		}

		return resultList;
	}

	private Document getDoc(String url) throws IOException, HttpClientException {
		byte[] resp = HttpClient5Helper.httpGet(url, null, null, cookieStore);
		String str = new String(resp);
		String respStr;
		if (str.contains("window.atob(")) {
			String[] strList = StringUtils.substringsBetween(str, "window.atob(\"", "\")");
			resp = Base64.decodeBase64(strList[0]);
			respStr = URIUtil.decode(new String(resp));
		} else {
			respStr = new String(resp);
		}

		return Jsoup.parse(respStr);
	}


}
