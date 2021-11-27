package cn.lucifer.demo.avmoo;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.HttpMethod;
import cn.lucifer.http.HttpSocket;
import cn.lucifer.util.HttpClientHelper;
import com.google.common.collect.Lists;
import org.apache.commons.httpclient.HttpException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvmooStar {
	private static final Map<String, String> httpHeads;

	static {
		httpHeads = new HashMap<>();
		httpHeads.put("cookie", "_ga=GA1.2.419230642.1619936650; dom3ic8zudi28v8lr6fgphwffqoz0j6c=b7aec789-77f8-4327-acaa-9861fc9a359b%3A2%3A1; AD_enterTime=1637941205; __test; __PPU___PPU_SESSION_URL=%2Fcn; AD_clic_j_POPUNDER=2; pnState={\"impressions\":0,\"delayStarted\":0,\"page\":\"/cn/search/%E4%B8%89%E4%B8%8A%E6%82%A0%E4%BA%9C\"}; AD_exoc_j_M_728x90=1; AD_juic_j_P_728x90=1; AD_exoc_j_L_728x90=3; AD_exoc_j_POPUNDER=2");
		httpHeads.put("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
	}

	private String name;
	private String urlStr;
	private URL url;

	/**
	 * 增量模式
	 */
	private boolean incrementalMode;

	public AvmooStar() {
	}

	public AvmooStar(String name, String urlStr) throws Exception {
		this();
		this.name = name;
		this.urlStr = urlStr;
		this.url = new URL(this.urlStr);
	}

	public AvmooStar(String name, String urlStr, boolean incrementalMode) throws Exception {
		this(name, urlStr);
		this.incrementalMode = incrementalMode;
	}

	public List<String> getAllMoviePageUrl() throws Exception {
		List<String> result = Lists.newArrayList();

		URL url = this.url;

		Document doc = getDoc(urlStr);

		result.addAll(findMovieBox(doc));

		if (this.incrementalMode) {
			return result;
		}

		int index = 0;
		for (String nextUrl = findNextPage(doc); nextUrl != null; nextUrl = findNextPage(doc)) {
//			if (index++ >= 40) {
//				break;
//			}

			String next = url.getProtocol() + "://" + url.getHost() + nextUrl;
			System.out.println("next=" + next);
			try {
				doc = getDoc(next);
			} catch (HttpException e) {
				if (e.getReasonCode() == 403) {
					System.err.println(e.toString());
					break;
				} else {
					throw e;
				}
			}
			result.addAll(findMovieBox(doc));
		}

		return result;
	}

	static Document getDoc(String url) throws IOException, HttpClientException {
		byte[] resp = HttpClientHelper.httpGet(url, null, httpHeads);
		return Jsoup.parse(new String(resp));
	}

	private List<String> findMovieBox(Document doc) {
		List<String> result = Lists.newArrayList();
		Elements movieBoxList = doc.select("a.movie-box");
		for (Element movieBox : movieBoxList) {
			result.add(url.getProtocol() + ":" + movieBox.attr("href"));
		}

		return result;
	}

	/**
	 * 这里是相对路径
	 *
	 * @return
	 */
	protected String findNextPage(Document doc) {
		Elements pageList = doc.select(".pagination li a");
		Element last = pageList.last();
		if (null == last) {
			return null;
		}
		if ("nextpage".equals(last.attr("name"))) {
			return last.attr("href");
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrlStr() {
		return urlStr;
	}

	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}

	public URL getUrl() {
		return url;
	}
}
