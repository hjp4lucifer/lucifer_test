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
import org.apache.commons.collections4.ListUtils;
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

/**
 * 抓取任务的调试入口。站点换域名/IP 时改这几处（都在 main 代码里，测试类无需改）：
 * <ul>
 * <li>磁力猫：{@link CilimaoApp} 的 BASE_URL</li>
 * <li>jayBot：{@link JayBot} 的 BASE_URL（含端口）</li>
 * <li>jayBot 的 COOKIE_DOMAIN：必须与 BASE_URL 的 host 完全一致，否则 cookie 不生效</li>
 * <li>换站点后重新导出 cookie，覆盖 src/test/resources/javabot3/javbot3_cookie.txt</li>
 * </ul>
 */
public class AutoFindToolsTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** 起始页，从 1 开始 */
	private static final int startPage = 1;
	/** 演员名 → jayBot 演员编号，来源 javabot3/actor_code_map.json */
	private final ImmutableMap<String, String> actorCodeMap = loadActorCodeMap();
	/**
	 * 起始番号（已去掉前后缀），空则从 startPage 第一条开始
	 * 如：JUR-417
	 */
	static final String startVideo = "";
	/** javabot3/javbot3_cookie.txt 里的完整 cookie 串，必须含 csrf_cookie */
	static final String javbot3_cookie = getJavbot3Cookie();
	/** 索引文件日期，对应 M:\limit\aaa\limit_mp4_20260419.txt 等 */
	static final String load_file_date = "20260419";
	/** 结果输出目录 */
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

	/**
	 * 批量按演员抓取：逐个演员生成一份 htm 结果。
	 * <p>
	 * actorNameList 两种填法：注释掉的 actorCodeMap.keySet() = 跑全部演员；或手写演员名（必须在 actor_code_map.json 里）。
	 * 演员编号由 actorCodeMap 按名字查出，传 null 会因 /actor/null 请求失败。
	 */
	@Test
	public void autoFind_findByAuthor_all() throws Exception {
		List<String> allList = Lists.newArrayList(actorCodeMap.keySet());
		List<String> actorNameList = Lists.newArrayList("桃乃木かな","篠田ゆう");

		List<String> targetList = ListUtils.removeAll(allList, actorNameList);

		for (String actorName : targetList) {
			logger.info("actor={}, code={}", actorName, actorCodeMap.get(actorName));
			// 每个演员独立实例，避免 cookie/状态互相影响
			LimitAutoFindTools tools = new LimitAutoFindTools(startPage, startVideo, javbot3_cookie, load_file_date, result_folder);

			// 第二个参数：演员作品列表与磁力搜索各自的最大翻页数
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
