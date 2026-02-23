package cn.lucifer.demo.http;

import cn.lucifer.demo.http.dict.CilimaoSearchTypeEnum;
import cn.lucifer.demo.http.domain.CilimaoLinkedInfo;
import cn.lucifer.demo.http.domain.JayBotItemInfo;
import cn.lucifer.util.CookiesUtils;
import com.alibaba.fastjson.JSON;
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
	static final String javbot3_cookie = "server_name_session=8be949241b0363e6537e06e054d75dc4; _clck=snbd7f^2^g3t^0^2027; b94d0f4097de070c3771d5969dd5ee4b=6fe8dd0d1e9fdb44105535dee5415e36; csrf_cookie=82e2bc4ff12b8a65e243907e1b299745; cqse=VjRWPVQ3WjcHKgIkBzpRY1ZmVG4ILVYhUjVUJgJ3BWpQPFFiBVQHblQzVCcAbVt/VjpRN1xpBz5TcQA3BTMGMAVkUGZWYQIyAjAEblI+U2FWYVZlVGFaNAcyAjEHNlFmVjVUNQg/VjRSZlRjAjAFYlBgUTQFOQdiVDRUJwBtW39WOlE1XGsHPlNxAGkFcgZdBWZQNVY0AnYCZAQqUi9TcVZuVnRUOFo8B2ICbQciUWZWY1R6CD5WZ1JhVHsCNQUwUGFRIgU/BzNUdVQ+ACVbNlYxUTRcYQcmUyYAcwVnBnAFWFAwVjcCYQJvBC1SflNoViZWPVQzWjwHZAJtByJRH1Y5VC4IZlY+UjxUNAIrBTZQfVE8BSsHL1QAVGwAOFtoVm9Rc1woByRTHQBUBSIGMwU3UH9WYAI/AiEEDlI1Uz1WY1YzVDlaLQcpAmEHNFF7VnZUFQh/ViJSPFQwAlMFZlAxUUcFYgdzVHhUMABlWztWLlE3XG0HJFN7AEsFSgZWBUpQHVZ8AiQCbQQwUjdTNlZ1VkBUZ1puBzoCOAcpUXJWFVQ8CH1WPVI9VDACKwU6UGFRIgU7BylUY1QwAGdbOVYuUTVcawczU3MAUwVjBmQFZlAjVjkCKwI0BGpSa1N9VmZWMVQgWjYHIgJtBzFRYVZsVHYIY1YzUiNUIQJbBWJQMFF4BWIHcVQ+VHEAL1suVjtRbVxhBzVTZAA3BTMGOgUxUGJWZAI8AjYEYlIvU2lWbFY9VCBaeAciAjIHclENVjJUNQh7VjNSclRuAncFOVBjUTYFKQclVGxUeA==; _clsk=2x680l^1771863491178^2^1^a.clarity.ms/collect";
	static final String load_file_date = "20260223";
	static final File result_folder = new File("M:\\limit\\aaa\\limit_search_result");

	@Test
	public void autoFind_uncensored() throws Exception {
		final String loadEndTime = "2026-01-15";
		final File oldFile = new File(result_folder, "uncensored_HD_error_20251025_150645.txt");

		LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie, load_file_date, result_folder);
		tools.autoFind(CilimaoSearchTypeEnum.uncensored_HD, loadEndTime, 84, oldFile);
	}

	@Test
	public void autoFind_findByAuthor() throws Exception {
		LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie, load_file_date, result_folder);
		tools.autoFindByAuthor("qvRpm", 100);
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
