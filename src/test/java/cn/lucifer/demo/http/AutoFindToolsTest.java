package cn.lucifer.demo.http;

import cn.lucifer.demo.http.dict.CilimaoSearchTypeEnum;
import cn.lucifer.demo.http.domain.CilimaoLinkedInfo;
import cn.lucifer.demo.http.domain.JayBotItemInfo;
import com.alibaba.fastjson.JSON;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class AutoFindToolsTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final int startPage = 1;
	/**
	 * 如：JUR-417
	 */
	static final String startVideo = "";
	static final String javbot3_cookie_token = "afbf30807b5575036eec31fd71809b2f";
	static final String load_file_date = "20260223";
	static final File result_folder = new File("M:\\limit\\aaa\\limit_search_result");

	@Test
	public void autoFind_uncensored() throws Exception {
		final String loadEndTime = "2026-01-15";
		final File oldFile = new File(result_folder, "uncensored_HD_error_20251025_150645.txt");

		LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie_token, load_file_date, result_folder);
		tools.autoFind(CilimaoSearchTypeEnum.uncensored_HD, loadEndTime, 84, oldFile);
	}

	@Test
	public void autoFind_findByAuthor() throws Exception {
		final String baseUrl = "https://javbot3.top/actor/qvRpm?t=exr";
	}

	@Test
	public void autoFind_hdd600() throws Exception {
		final String loadEndTime = "2021-10-20";
		LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie_token, load_file_date, result_folder);
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
		JayBot jayBot = new JayBot(cookieStore, javbot3_cookie_token);

		List<String> urlList = jayBot.search(keyword);
		logger.info("urlList = {}", JSON.toJSONString(urlList));
	}

	@Test
	public void jayBot_searchV2() throws Exception {
		final String keyword = "SONE-915";
		BasicCookieStore jayBotCookieStore = new BasicCookieStore();
		{
			BasicClientCookie cookie = new BasicClientCookie("csrf_cookie", javbot3_cookie_token);
			cookie.setDomain("javbot3.top");
			jayBotCookieStore.addCookie(cookie);
		}
		JayBot jayBot = new JayBot(jayBotCookieStore, javbot3_cookie_token);

		List<JayBotItemInfo> infoList = jayBot.searchV2(keyword);
		logger.info("infoList = {}", JSON.toJSONString(infoList));
	}

	@Test
	public void jayBot_detail() throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
			JayBot jayBot = new JayBot(cookieStore, javbot3_cookie_token);

		String url = "/item/ZxYjn";
		JayBotItemInfo info = jayBot.getDetail(url);

		logger.info("info = {}", JSON.toJSONString(info));
	}


}
