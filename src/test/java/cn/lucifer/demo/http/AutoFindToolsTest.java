package cn.lucifer.demo.http;

import cn.lucifer.demo.http.dict.CilimaoSearchTypeEnum;
import cn.lucifer.demo.http.domain.CilimaoLinkedInfo;
import cn.lucifer.demo.http.domain.JayBotItemInfo;
import cn.lucifer.util.CookiesUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertNotNull;

public class AutoFindToolsTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final int startPage = 1;
	/**
	 * 如：JUR-417
	 */
	static final String startVideo = "";
	static final String javbot3_cookie = "_clck=snbd7f^2^g4t^0^2027; server_name_session=493cc81fe166d7aafe7c4238c10e811c; 8734c89f8a46a22cb13aa917d4ae2005=5fcf1061a0c5cdaebd3e3aa7f2a762d5; csrf_cookie=f87a7ae537328f1be1ef4655429ca8b2; cqse=AGJROlc0B2oCLwYgXWAENldnBz0GIwRzXzgFdwRxAm0BbQQ3B1YBaAtsAnFXOldzUz9TNQA1CDFScFdiVGdUMlNjVWcCMVNtV2QGPwI+VjcAZlExVzQHNQJsBmVdOQQ2VzQHPgY0BDlfbQU1BDACPAEwBDsHOAE0Cz8CcVc6V3NTP1M3ADcIMVJwVz5UI1QPUzBVMAJgUydXMQYoAn9WdAA4UXNXOwdhAmcGaV14BDNXYgcpBjAENV9sBSoEMwI3ATAEdwc9ATULKgJoV3JXOlM0UzYAPQgpUidXJFQ2VCJTDlU1AmNTMFc6Bi8CLlZtAHBROlcwB2ECYQZpXXgESlc4B30GaARsXzEFZQQtAjEBLARpBykBKQtfAjpXb1dkU2pTcQB0CCtSHFcDVHNUYVNhVXoCNFNuV3QGDAJlVjgANVE0VzoHcAIsBmVdbgQuV3cHRgZxBHBfMQVhBFUCYQFgBBIHYAF1CycCZlcyVzdTK1M1ADEIK1J6VxxUG1QEUxxVGAIoU3VXOAYyAmdWMwAjUUdXZAczAj8GPF1zBCdXFAdvBnMEb18wBWEELQI9ATAEdwc5AS8LPAJmVzBXNVMrUzcANwg8UnJXBFQyVDZTMFUmAm1TeldhBmgCO1Z4ADBRNlcjB2sCJwZpXWsENFdtByUGbQRhXy4FcARdAmUBYQQtB2ABdwthAidXeFciUz5TbwA9CDpSZVdgVGVUZVNgVWcCPFNmV2AGYAJ/VmwAOlE6VyMHJQInBjZdKARYVzMHZgZ1BGFffwU/BHECPgEyBGMHKwEjCzMCLg==";
	static final String load_file_date = "20260306";
	static final File result_folder = new File("M:\\limit\\aaa\\limit_search_result");


	private final ImmutableMap<String, String> actorCodeMap = ImmutableMap.<String, String>builder()
			.put("桃乃木かな","8gWEp")
			.put("篠田ゆう","8KPWy")
			.put("小島みなみ","Qn3kB")
			.put("五日市芽依","OE2QM")
			.put("女神ジュン","Dg2LN")
			.put("七瀬アリス","qgxmw")
			.put("三田真鈴","qvRpm")
			.put("藍芽みずき","jL1gj")
			.put("夢実かなえ","Q9Jx9")
			.put("海老咲あお","QpwY2")
			.put("釈アリス","9lXP2")
			.put("小那海あや","ZNmJ5")
			.put("楓カレン","Avqj2")
			.put("楓ふうあ","8K09m")
			.put("絵恋空","zADe0")
			.put("うんぱい","OEXrO")
			.build();

	@Test
	public void autoFind_uncensored() throws Exception {
		final String loadEndTime = "2026-03-25";
		final File oldFile = new File(result_folder, "uncensored_HD_error_20251025_150645.txt");

		LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie, load_file_date, result_folder);
		tools.autoFind(CilimaoSearchTypeEnum.uncensored_HD, loadEndTime, 84, oldFile);
	}

	@Test
	public void autoFind_findByAuthor_all() throws Exception {
		List<String> actorNameList = Lists.newArrayList(actorCodeMap.keySet());
		actorNameList.remove("桃乃木かな");
		actorNameList.remove("篠田ゆう");

		for (String actorName : actorNameList) {
			logger.info("actor={}, code={}", actorName, actorCodeMap.get(actorName));
			LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie, load_file_date, result_folder);

			tools.autoFindByAuthor(actorCodeMap.get(actorName), 100);
		}
	}

	@Test
	public void autoFind_findByAuthor() throws Exception {
		LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie, load_file_date, result_folder);
		tools.autoFindByAuthor(actorCodeMap.get("小那海あや"), 100);
	}

	@Test
	public void autoFind_hdd600() throws Exception {
		final String loadEndTime = "2021-10-20";
		LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie, load_file_date, result_folder);
		tools.autoFind(CilimaoSearchTypeEnum.hdd600, loadEndTime, 25, null);
	}


	@Test
	public void cilimaoApp() throws Exception {
		CilimaoApp cilimaoApp = new CilimaoApp(CilimaoSearchTypeEnum.hdd600, null);

		for (int i = 2; i <= 2; i++) {
			List<CilimaoLinkedInfo> resultList = cilimaoApp.getLinkedInfoList(i);
			logger.info("resultList = {}", JSON.toJSONString(resultList));
		}
	}


	@Test
	public void jayBot_search() throws Exception {
		final String keyword = "SONE-915";
		BasicCookieStore cookieStore = new BasicCookieStore();
		JayBot jayBot = new JayBot(CookiesUtils.getCookieStore("javbot3.top", javbot3_cookie));

		List<String> urlList = jayBot.search(keyword);
		logger.info("urlList = {}", JSON.toJSONString(urlList));
	}

	@Test
	public void jayBot_searchV2() throws Exception {
		final String keyword = "SONE-915";
		JayBot jayBot = new JayBot(CookiesUtils.getCookieStore("javbot3.top", javbot3_cookie));

		List<JayBotItemInfo> infoList = jayBot.searchV2(keyword);
		logger.info("infoList = {}", JSON.toJSONString(infoList));
	}

	@Test
	public void jayBot_detail() throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		JayBot jayBot = new JayBot(CookiesUtils.getCookieStore("javbot3.top", javbot3_cookie));

		String url = "/item/ZxYjn";
		JayBotItemInfo info = jayBot.getDetail(url);

		logger.info("info = {}", JSON.toJSONString(info));
	}


}
