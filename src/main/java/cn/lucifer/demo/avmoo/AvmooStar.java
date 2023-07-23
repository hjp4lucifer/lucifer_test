package cn.lucifer.demo.avmoo;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.HttpMethod;
import cn.lucifer.http.HttpSocket;
import cn.lucifer.util.HttpClient5Helper;
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
		httpHeads.put("cookie", "_ga=GA1.2.1024951988.1682909600; _gid=GA1.2.1512620022.1690035515; AD_exoc_j_M_728x90=1; AD_javu_j_P_728x90=1; AD_clic_j_POPUNDER=2; AD_exoc_j_POPUNDER=2; AD_adst_j_POPUNDER=2; dom3ic8zudi28v8lr6fgphwffqoz0j6c=5a04e05a-d81d-4556-8789-77c523655c91%3A3%3A1; AD_javu_j_POPUNDER=1; AD_enterTime=1690084268; AD_juic_j_L_728x90=0; AD_exoc_j_L_728x90=1; _gat=1; _ga_7JMF9KBRFV=GS1.2.1690083482.3.1.1690084269.0.0.0");
		httpHeads.put("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
		httpHeads.put("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		httpHeads.put("accept-encoding","gzip, deflate, br");
		httpHeads.put("accept-language","zh-CN,zh;q=0.9,en;q=0.8");
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
			if (index++ >= 4) {
				break;
			}

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
		byte[] resp = HttpClient5Helper.httpGet(url, null, httpHeads);
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
