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
	static final String javbot3_cookie = "server_name_session=8be949241b0363e6537e06e054d75dc4; csrf_cookie=017da5f8d853c670c115c4932c600bb2; _clck=snbd7f^2^g3t^0^2027; 3ad6828500a26363477e2631874c27d6=84e451d279977bd1703c50244a0cea9d; cqse=UzEGbVMwA24CL1RyVGkCMFNjATsFIFEmUDcCcFYjVzgLZwEyAlMPZgNkUyAHalVxUj5WMAQxCDEFJwNlV2cAMABnBTwKPAIxVDNVMAFqA2ZTNQZjUzQDZwJkVDFUZwIyU2IBMQVnUTBQNgIyVmJXZgtrAW0Cbg84A2VTIAdqVXFSPlYyBDMIMQUnA2pXIABbAGMFYApoAnZUMlV7AXwDIVNrBiRTPwNlAmdUO1RxAjVTZgEvBTNRYFBjAi1WYVdiCzoBcgI4DzsDIlM5ByJVOFI1VjMEOQgpBXADcFc1AHYAXQVlCmsCYVQ5VXwBLQM4UyMGbVM0A2UCYVQ7VHECTFM8AXsFa1E5UD4CYlZ/V2QLJgFsAiwPJwNXU2sHP1VmUmtWdARwCCsFSwNXV3AANQAyBSoKPAI/VHdVXwFmA21TZgZjUz4DdAIsVDdUZwIoU3MBQAVyUSVQPgJmVgdXNAtqARcCZQ97Ay9TNwdiVTVSKlYwBDUIKwUtA0hXGABQAE8FSAogAiRUO1VhAWQDZlNwBhBTYAM3Aj9UblR6AiFTEAFpBXBROlA/AmZWf1doCzoBcgI8DyEDNFM3B2BVN1IqVjIEMwg8BSUDUFcxAGIAYwV2CmUCK1RiVTsBOAMtU2MGYVMnA28CJ1Q7VGICMlNpASMFblE0UCECd1YPVzALawEoAmUPeQNpU3YHKFUgUj9WagQ5CDoFMgM0V2EAPAA2BTQKOgIwVGJVMwF8AzlTaQZtUycDIQInVGRUIQJeUzcBYAV2UTRQcAI4ViNXaws4AWYCLg8tAztTfw==; _clsk=as8szw^1771840646798^2^1^a.clarity.ms/collect";
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
