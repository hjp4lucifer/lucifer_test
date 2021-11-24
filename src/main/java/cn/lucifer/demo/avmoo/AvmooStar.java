package cn.lucifer.demo.avmoo;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.HttpMethod;
import cn.lucifer.http.HttpSocket;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class AvmooStar {
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
		this.name = name;
		this.urlStr = urlStr;
		this.url = new URL(this.urlStr);
	}

	public AvmooStar(String name, String urlStr, boolean incrementalMode) throws Exception {
		this(name, urlStr);
		this.incrementalMode = incrementalMode;
	}

	public List<String> getAllMoviePageUrl(HttpSocket httpSocket) throws Exception {
		List<String> result = Lists.newArrayList();

		URL url = this.url;

		Document doc = getDoc(httpSocket, url);

		result.addAll(findMovieBox(doc));

		if (this.incrementalMode) {
			return result;
		}

		int index = 0;
		for (String nextUrl = findNextPage(doc); nextUrl != null; nextUrl = findNextPage(doc)) {
			if (index++ >= 30) {
				break;
			}

			String next = url.getProtocol() + "://" + url.getHost() + nextUrl;
			System.out.println("next=" + next);
			url = new URL(next);
			try {
				doc = getDoc(httpSocket, url);
			} catch (org.jsoup.HttpStatusException e) {
				if (e.getStatusCode() == 403) {
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

	static Document getDoc(HttpSocket httpSocket, URL url) throws IOException, HttpClientException {
		Document doc;
		if (null == httpSocket) {
			doc = Jsoup.parse(url, 300000);
		} else {
			byte[] responeBytes = httpSocket.request(HttpMethod.GET, url.getPath(), null, null);
			doc = Jsoup.parse(new String(responeBytes));
		}
		return doc;
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
