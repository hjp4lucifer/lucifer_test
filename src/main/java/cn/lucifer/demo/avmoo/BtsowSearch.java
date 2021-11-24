package cn.lucifer.demo.avmoo;

import cn.lucifer.http.HttpHelper;
import cn.lucifer.http.HttpMethod;
import cn.lucifer.util.HttpClientHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BtsowSearch {

	private final String base_url = "https://btsow.rest/search/Uncensored%20";
	private final Map<String, String> httpHeads;

	public BtsowSearch() {
		httpHeads = new HashMap<>();
		httpHeads.put("cookie", "_ga=GA1.2.1902309668.1637639081; _gid=GA1.2.1783056452.1637639081; __PPU_SESSION_1_470916=1637639081272|1|1637639132888|1|1; AD_clic_b_POPUNDER=2; dom3ic8zudi28v8lr6fgphwffqoz0j6c=b7aec789-77f8-4327-acaa-9861fc9a359b%3A2%3A1; AD_adst_b_POPUNDER=2; AD_adma_b_POPUNDER=2; AD_popc_b_POPUNDER=1; popcashpu=1; AD_jav_b_POPUNDER=1; AD_javu_b_MD_B_728x90=5; AD_enterTime=1637681863; AD_juic_b_MD_T_728x90=21; AD_jav_b_MD_B_728x90=16; AD_exoc_b_PLAY=66; AD_jav_b_SM_B_728x90=16; AD_juic_b_SM_T_728x90=14; AD_javu_b_SM_B_728x90=27; _gat=1");
		httpHeads.put("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
	}

	public boolean hasUncensored(String movieName) throws Exception {
		String url = base_url + movieName;
		byte[] resp = HttpClientHelper.httpGet(url,null, httpHeads);
		Document doc = Jsoup.parse(new String(resp));
//		Document doc = AvmooStar.getDoc(null, new URL(url));

		Elements aList = doc.select(".row a");
		if (aList.isEmpty()) {
			return false;
		}
		for (Element a : aList) {
			String title = a.attr("title");
			if (title.contains("Uncensored")) {
				System.out.println(title);
				return true;
			}
		}

		return false;
	}
}
