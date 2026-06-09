package cn.lucifer.demo.http;

import cn.lucifer.demo.http.dict.CilimaoSearchTypeEnum;
import cn.lucifer.demo.http.domain.CilimaoLinkedInfo;
import cn.lucifer.demo.http.domain.JayBotItemInfo;
import cn.lucifer.util.ConfigUtils;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertNotNull;

public class AutoFindToolsTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final int startPage = 1;
	private final ImmutableMap<String, String> actorCodeMap = loadActorCodeMap();
	/**
	 * 如：JUR-417
	 */
	static final String startVideo = "";
	static final String javbot3_cookie = getJavbot3Cookie();
	static final String load_file_date = "20260419";
	static final File result_folder = new File("M:\\limit\\aaa\\limit_search_result");

	private static String getJavbot3Cookie() {
		try {
			return ConfigUtils.loadStrResource(AutoFindToolsTest.class, "javabot3/javbot3_cookie.txt");
		} catch (IOException e) {
			throw new RuntimeException("读取javabot3_cookie.txt失败", e);
		}
	}


	private static ImmutableMap<String, String> loadActorCodeMap() {
		try {
			String str = ConfigUtils.loadStrResource(AutoFindToolsTest.class, "javabot3/actor_code_map.json");
			Map<String, String> map = JSON.parseObject(str, new com.alibaba.fastjson.TypeReference<Map<String, String>>() {});
			return ImmutableMap.copyOf(map);
		} catch (IOException e) {
			throw new RuntimeException("读取actor_code_map.json失败", e);
		}
	}

	@Test
	public void autoFind_uncensored() throws Exception {
		final String loadEndTime = "2026-04-10";
		final File oldFile = new File(result_folder, "uncensored_HD_error_20251025_150645.txt");

		LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie, load_file_date, result_folder);
		tools.autoFind(CilimaoSearchTypeEnum.uncensored_HD, loadEndTime, 84, oldFile);
	}

	@Test
	public void autoFind_findByAuthor_all() throws Exception {
//		List<String> actorNameList = Lists.newArrayList(actorCodeMap.keySet());
		List<String> actorNameList = Lists.newArrayList("桃乃木かな");

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
		JayBot jayBot = new JayBot(CookiesUtils.getCookieStore(JayBot.COOKIE_DOMAIN, javbot3_cookie));

		List<String> urlList = jayBot.search(keyword);
		logger.info("urlList = {}", JSON.toJSONString(urlList));
	}

	@Test
	public void jayBot_searchV2() throws Exception {
		final String keyword = "SONE-915";
		JayBot jayBot = new JayBot(CookiesUtils.getCookieStore(JayBot.COOKIE_DOMAIN, javbot3_cookie));

		List<JayBotItemInfo> infoList = jayBot.searchV2(keyword);
		logger.info("infoList = {}", JSON.toJSONString(infoList));
	}

	@Test
	public void jayBot_detail() throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		JayBot jayBot = new JayBot(CookiesUtils.getCookieStore(JayBot.COOKIE_DOMAIN, javbot3_cookie));

		String url = "/item/ZxYjn";
		JayBotItemInfo info = jayBot.getDetail(url);

		logger.info("info = {}", JSON.toJSONString(info));
	}


}
