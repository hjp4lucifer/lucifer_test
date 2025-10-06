package cn.lucifer.demo.http;

import cn.lucifer.demo.http.domain.CilimaoLinkedInfo;
import cn.lucifer.demo.http.domain.JayBotItemInfo;
import cn.lucifer.http.HttpClientException;
import cn.lucifer.util.StrUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class AutoFindToolsTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@Test
	public void autoFind() throws Exception {
		File limitGirlFile = new File("M:\\limit\\aaa\\limit_girl_20250928.txt");
		List<String> limitGirlList = FileUtils.readLines(limitGirlFile, "utf-8");

		Map<String, String> limitGirlMap = Maps.newHashMapWithExpectedSize(limitGirlList.size());
		for (String girl : limitGirlList) {
			String[] split = StringUtils.split(girl, '\t');
			limitGirlMap.put(split[0], split[1]);
		}

		final String loadEndTime = "2025-09-13";
		final String startVideo = "300MIUM-1251";

		final List<String> outLineList = Lists.newArrayList();
		final File oldFile = new File("M:\\limit\\aaa\\limit_search_result\\error_20251006_220526.txt");
		if (oldFile.exists()) {
			outLineList.addAll(FileUtils.readLines(oldFile, "utf-8"));
			outLineList.add("\n\n\n\n\n\n");
		}

		CilimaoApp cilimaoApp = new CilimaoApp(loadEndTime, new BasicCookieStore());
		BasicCookieStore jayBotCookieStore = new BasicCookieStore();
		{
			BasicClientCookie cookie = new BasicClientCookie("csrf_cookie", "418d2a9f76559a65b96e2e04496ce2a4");
			cookie.setDomain("javbot3.top");
			jayBotCookieStore.addCookie(cookie);
		}

		JayBot jayBot = new JayBot(jayBotCookieStore);
		boolean isFirst = false;
		for (int i = 1; i <= 100; i++) {
			List<CilimaoLinkedInfo> linkedInfoList = cilimaoApp.getLinkedInfoList(i);
			logger.info("page={}, linkedInfoList = {}", i, JSON.toJSONString(linkedInfoList));

			for (CilimaoLinkedInfo linkedInfo : linkedInfoList) {
				String name = StringUtils.removeEnd(linkedInfo.name, "-uncensored-HD");
				if (startVideo.equals(name)) {
					isFirst = false;
				} else if (isFirst) {
					// 快进到开始文件
					logger.info("skip video file ={}", linkedInfo.name);
					continue;
				}

				List<JayBotItemInfo> videoInfoList = searchV2(jayBot, name, outLineList, i);

				if (CollectionUtils.isEmpty(videoInfoList)) {
					// 搜索不到结果
					JayBotItemInfo videoInfo = new JayBotItemInfo();
					videoInfo.actress = "-------";
					videoInfo.name = "-------";
					videoInfo.score = "-------";

					videoInfoList = Lists.newArrayList(videoInfo);


				}

				for (JayBotItemInfo videoInfo : videoInfoList) {
					StrBuilder outLine = new StrBuilder();

					String girlRating = limitGirlMap.get(videoInfo.actress);
					// 评分
					outLine.append(StringUtils.defaultString(girlRating, "unknowns")).append('\t');
					outLine.append(videoInfo.actress).append('\t');
					outLine.append(linkedInfo.name).append('\t');
					outLine.append(videoInfo.score).append('\t');
					outLine.append(linkedInfo.url).append('\t');
					outLine.append(videoInfo.name);

					String str = outLine.toString();
					logger.info("video={}", str);
					outLineList.add(str);

					sleepRandom(false);
				}
			}
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		final String outFn = StrUtils.generateMessage("M:\\limit\\aaa\\limit_search_result\\limit_search_result_{}.txt", dateFormat.format(new Date()));

		FileUtils.writeLines(new File(outFn), "utf-8", outLineList);
	}

	private List<JayBotItemInfo> searchV2(JayBot jayBot, String name, List<String> outLineList, int page) throws Exception {
		Exception ex = null;
		for (int i = 0; i < 5; i++) {

			try {
				return jayBot.searchV2(name);
			} catch (Exception e) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				final String outFn = StrUtils.generateMessage("M:\\limit\\aaa\\limit_search_result\\error_{}.txt", dateFormat.format(new Date()));
				outLineList.add(StrUtils.generateMessage("last page={}", page));
				FileUtils.writeLines(new File(outFn), "utf-8", outLineList);

				for (int j = 0; j < 30; j++) {
					sleepRandom(true);
				}

				ex = e;
			}
		}
		throw ex;
	}

	@Test
	public void cilimaoApp() throws Exception {
		final String loadEndTime = "2025-10-04";
		CilimaoApp cilimaoApp = new CilimaoApp(loadEndTime, null);

		for (int i = 2; i <= 2; i++) {
			List<CilimaoLinkedInfo> resultList = cilimaoApp.getLinkedInfoList(i);
			logger.info("resultList = {}", JSON.toJSONString(resultList));
		}
	}


	@Test
	public void jayBot_search() throws Exception {
		final String keyword = "SONE-915";
		BasicCookieStore cookieStore = new BasicCookieStore();
		JayBot jayBot = new JayBot(cookieStore);

		List<String> urlList = jayBot.search(keyword);
		logger.info("urlList = {}", JSON.toJSONString(urlList));
	}

	@Test
	public void jayBot_searchV2() throws Exception {
		final String keyword = "SONE-915AAA";
		BasicCookieStore cookieStore = new BasicCookieStore();
		JayBot jayBot = new JayBot(cookieStore);

		List<JayBotItemInfo> infoList = jayBot.searchV2(keyword);
		logger.info("infoList = {}", JSON.toJSONString(infoList));
	}

	@Test
	public void jayBot_detail() throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		{
			BasicClientCookie cookie = new BasicClientCookie("cqse", "A2EIY1MwAWwALVRyBDkFN1dnAjgJLAF2XzgFdwRxB2gCblJhVQRTOgFmUyBWO1dzUDxUMlBlVm8HJQQzUmYMMARlBTIHOVc1ADJdMVNrA2EDYwhoU2EBMABvVDQEOwVnV2ICYAlsATdfbwVmBDsHNQI2Uj5VaVNlATBTIFY7V3NQPFQwUGdWbwclBG1SJQxXBGcFYAdlVyMAZl1zUy4DIQM7CCpTPwFnAGVUOwQhBTJXYgIsCT8BMF9sBSoEMwcyAjNSIVVvU2cBIFM5VnNXOlA3VDFQbVZ3B3IEd1IwDHoEWQVlB2ZXNABtXXRTfwM4A3MIY1M0AWcAY1Q7BCEFS1c4AngJZwFpXzEFZQQtBzQCL1I/VXtTewFVU2tWbldkUGlUdlAkVnUHSQRQUnUMOQQ2BSoHMVdqACNdV1M0A20DNghtUz4BdgAuVDcENwUvV3cCQwl+AXVfMQVhBFUHZAJjUkRVMlMnAS1TN1YzVzdQKFQyUGFWdQcvBE9SHQxcBEsFSActV3EAb11pUzYDZgMgCB5TYAE1AD1UbgQqBSZXFAJqCXwBal8wBWEELQc4AjNSIVVrU30BNlM3VjFXNVAoVDBQZ1ZiBycEV1I0DG4EZwV2B2hXfgA2XTNTagMtAzMIb1MnAW0AJVQ7BDIFNVdtAiAJYgFkXy4FcARdB2ACYlJ7VTJTJQFrU3ZWeVciUD1UaFBtVmQHMAQxUmwMPwQyBTMHNVdjADddO1MuAzkDOQhjUycBIwAlVGQEcQVZVzMCYwl6AWRffwU/BHEHOwIxUjVVeVNxATlTfw==");
			cookie.setDomain("javbot3.top");
			cookieStore.addCookie(cookie);
		}
		{
			BasicClientCookie cookie = new BasicClientCookie("_clck", "c443jc^1759747533298^7^1^n.clarity.ms/collect");
			cookie.setDomain("javbot3.top");
			cookieStore.addCookie(cookie);
		}
		{
			BasicClientCookie cookie = new BasicClientCookie("csrf_cookie", "418d2a9f76559a65b96e2e04496ce2a4");
			cookie.setDomain("javbot3.top");
			cookieStore.addCookie(cookie);
		}
		{
			BasicClientCookie cookie = new BasicClientCookie("_clsk", "snbd7f^2^fzx^0^2027");
			cookie.setDomain("javbot3.top");
			cookieStore.addCookie(cookie);
		}
		JayBot jayBot = new JayBot(cookieStore);

		String url = "/item/ZxYjn";
		JayBotItemInfo info = jayBot.getDetail(url);

		logger.info("info = {}", JSON.toJSONString(info));
	}

	protected void sleepRandom(boolean isLongTime) {
		try {
			long millis;
			if (isLongTime) {
				millis = (long) (3000 + Math.random() * 10000);
			} else {
				millis = (long) (500 + Math.random() * 1000);
			}
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
}
