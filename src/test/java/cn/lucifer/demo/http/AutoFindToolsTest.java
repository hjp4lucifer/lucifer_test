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
	static final String javbot3_cookie = "server_name_session=e792d82e9c1b6ee5dcef9abb425c22a2; 6e8680a6d5248bc968590e6e689a0b9a=6317e64ba6f77218ae659f2fe0b0aa92; csrf_cookie=d578ea93592d3bef4d7ab59ec1023871; cqse=VjRVPgVmBmtadwQiUWwHNV1tUmgILQF2XzhSIAB1AG8BbQY1Dl9UPQdgVSZTPlJ2VTkFY1NmAThRc1BlVGoNPVEyUTYHYwIyATBUPlQ4BGVWZlUyBTAGZVo9BDNRZAdhXThSYAg/ATxfa1JrADIAYAFjBmoOMFRnBzVVJlM+UnZVOQVhU2QBOFFzUDlUIw1WUTJRNAdlAnYBZ1R6VCkEJlZuVXcFaQZgWj8Ea1F0BzBdaFJ8CD4BMF9sUn0ANwA1ATAGdQ40VGAHJlU/U3ZSP1UyBWBTbgEgUSRQI1Q2DXtRDFExB2YCYQFsVH1UeAQ/ViZVPgViBmBaOQRrUXQHSV0yUigIZgFpXzFSMgApADMBLAZrDiBUfAdTVW1Ta1JhVWwFJ1MnASJRH1AEVHMNOFFjUX4HMQI/ASJUXlQzBGpWY1UwBWgGcVp0BGdRYgctXX1SEwh/AXVfMVI2AFEAYwFgBhAOaVQgBytVMVM2UjJVLQVjU2IBIlF5UBtUGw1dUR5RHActAiQBblRgVDEEYVZ1VUMFNgYyWmcEPlF/ByRdHlI6CH0Bal8wUjYAKQA/ATAGdQ4wVHoHMFUxUzRSMFUtBWFTZAE1UXFQA1QyDW9RMlEiB2gCKwE3VDpUbQQqVmZVMgVxBmpafwRrUWcHN11nUnAIYwFkXy5SJwBZAGcBYQYvDmlUIgdtVXBTfFInVTgFOVNuATNRZlBnVGENO1FjUWkHNwI0ATpUMlQpBD5WbFU+BXEGJFp/BDRRJAdbXTlSMwh7AWRff1JoAHUAPAEyBmEOIlR2Bz9VeQ==; _clck=snbd7f^2^g3x^0^2027; _clsk=1xa2rxv^1772209614681^1^1^q.clarity.ms/collect";
	static final String load_file_date = "20260223";
	static final File result_folder = new File("M:\\limit\\aaa\\limit_search_result");

	@Test
	public void autoFind_uncensored() throws Exception {
		final String loadEndTime = "2026-02-15";
		final File oldFile = new File(result_folder, "uncensored_HD_error_20251025_150645.txt");

		LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie, load_file_date, result_folder);
		tools.autoFind(CilimaoSearchTypeEnum.uncensored_HD, loadEndTime, 84, oldFile);
	}

	@Test
	public void autoFind_findByAuthor() throws Exception {
		LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie, load_file_date, result_folder);
		tools.autoFindByAuthor("8KPWy", 100);
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
