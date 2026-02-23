package cn.lucifer.demo.http;

import cn.lucifer.demo.http.dict.CilimaoSearchTypeEnum;
import cn.lucifer.demo.http.domain.CilimaoLinkedInfo;
import cn.lucifer.demo.http.domain.JayBotActorPageResult;
import cn.lucifer.demo.http.domain.JayBotItemInfo;
import cn.lucifer.util.CookiesUtils;
import cn.lucifer.util.StrUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LimitAutoFindTools {

	private static final Logger logger = LoggerFactory.getLogger(LimitAutoFindTools.class);

	final File folder = new File("M:\\limit\\aaa");

	private final int startPage;
	private final String startVideo;
	private final String javbot3CookieToken;
	private final String loadFileDate;
	private final File resultFolder;
	private final BasicCookieStore jayBotCookieStore;

	public LimitAutoFindTools(int startPage, String startVideo, String javbot3Cookie, String loadFileDate, File resultFolder) {
		this.startPage = startPage;
		this.startVideo = startVideo;
		BasicCookieStore cookieStore = CookiesUtils.getCookieStore("javbot3.top", javbot3Cookie);
		String javbot3CookieToken = CookiesUtils.getByName(cookieStore, "csrf_cookie");
		if (null == javbot3CookieToken) {
			throw new IllegalArgumentException("javbot3_cookie_token is null");
		}
		this.jayBotCookieStore = cookieStore;
		this.javbot3CookieToken = javbot3CookieToken;
		this.loadFileDate = loadFileDate;
		this.resultFolder = resultFolder;
	}


	public void autoFind(CilimaoSearchTypeEnum searchTypeEnum, String loadEndTime,
						 int maxPage, File oldFile) throws Exception {
		final Map<String, String> limitGirlMap = loadFile("limit_girl_{}.txt");
		final Map<String, String> limitMp4Map = loadFile("limit_mp4_{}.txt");

		File[] mp4FileArray = folder.listFiles(f -> f.getName().endsWith(".mp4"));
		if (null != mp4FileArray) {
			for (File mp4File : mp4FileArray) {
				String name = mp4File.getName();
				limitMp4Map.put(name, "unknowns");
			}
		}

		final List<String> outLineList = Lists.newArrayList();
		if (null != oldFile && oldFile.exists()) {
			outLineList.addAll(FileUtils.readLines(oldFile, "utf-8"));
			outLineList.add("\n\n\n\n\n\n");
		}

		CilimaoApp cilimaoApp = new CilimaoApp(searchTypeEnum, new BasicCookieStore());

		JayBot jayBot = new JayBot(jayBotCookieStore);
		boolean isFirst = StringUtils.isNotBlank(startVideo);

		loopA:
		for (int i = startPage; i <= maxPage; i++) {
			List<CilimaoLinkedInfo> linkedInfoList = cilimaoApp.getLinkedInfoList(i);
			logger.info("page={}, linkedInfoList = {}", i, JSON.toJSONString(linkedInfoList));

			for (CilimaoLinkedInfo linkedInfo : linkedInfoList) {
				if (loadEndTime.equals(linkedInfo.createTime)) {
					logger.info("到达loadEndTime!!!!");
					break loopA;
				}

				String name = StringUtils.removeEnd(linkedInfo.name, searchTypeEnum.getSuffix());
				name = StringUtils.removeStart(name, searchTypeEnum.getPrefix());
				if (startVideo.equals(name)) {
					isFirst = false;
				} else if (isFirst) {
					// 快进到开始文件
					logger.info("skip video file ={}", linkedInfo.name);
					continue;
				}

				List<JayBotItemInfo> videoInfoList = searchV2(jayBot, name, outLineList, i, searchTypeEnum);

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

					if (limitMp4Map.containsKey(linkedInfo.name)) {
						outLine.append("exists!!!").append('\t');
					} else if (StringUtils.isNotEmpty(searchTypeEnum.getToMp4Suffix())) {
						String mp4Name = name + searchTypeEnum.getToMp4Suffix();
						if (limitMp4Map.containsKey(mp4Name)) {
							outLine.append("exists!!!").append('\t');
						}
					}

					String girlRating = limitGirlMap.get(videoInfo.actress);
					// 评分
					outLine.append(StringUtils.defaultString(girlRating, "unknowns")).append('\t');
					outLine.append(videoInfo.actress).append('\t');
					outLine.append(linkedInfo.name).append('\t');

					outLine.append(videoInfo.score).append('\t');
					outLine.append(linkedInfo.url).append('\t');
					outLine.append(linkedInfo.createTime).append('\t');
					outLine.append(videoInfo.name);

					String str = outLine.toString();
					logger.info("video={}", str);
					outLineList.add(str);

					sleepRandom(false);
				}
			}
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		final String outFn = StrUtils.generateMessage("{}_result_{}.txt",
				searchTypeEnum.name(), dateFormat.format(new Date()));

		FileUtils.writeLines(new File(resultFolder, outFn), "utf-8", outLineList);
	}

	public void autoFindByAuthor(String actorCode,
								 int maxPage) throws Exception {
		final Map<String, String> limitMp4Map = loadFile("limit_mp4_{}.txt");

		File[] mp4FileArray = folder.listFiles(f -> f.getName().endsWith(".mp4"));
		if (null != mp4FileArray) {
			for (File mp4File : mp4FileArray) {
				String name = mp4File.getName();
				limitMp4Map.put(name, "unknowns");
			}
		}

		final List<String> outLineList = Lists.newArrayList();

		JayBot jayBot = new JayBot(jayBotCookieStore);
		String actorName = StringUtils.EMPTY;

		// 手动控制分页
		List<JayBotItemInfo> videoList = Lists.newArrayList();
		int currentPage = 1;
		while (currentPage <= maxPage) {
			JayBotActorPageResult pageResult = jayBot.getByActor(actorCode, currentPage);
			if (1 == currentPage) {
				actorName = pageResult.actorName;
				outLineList.add(pageResult.actorName + "\n");
			}
			if (pageResult.items == null || pageResult.items.isEmpty()) {
				break;
			}
			videoList.addAll(pageResult.items);
			logger.info(StrUtils.generateMessage("videoList page={}, size={}, total={}", currentPage, pageResult.items.size(), videoList.size()));

			if (pageResult.nextPage <= 0) {
				break;
			}
			currentPage++;

			sleepRandom(false);
		}

		logger.info("videoList total = {}", JSON.toJSONString(videoList));


		CilimaoSearchTypeEnum searchTypeEnum = CilimaoSearchTypeEnum.base64;
		CilimaoApp cilimaoApp = new CilimaoApp(searchTypeEnum, new BasicCookieStore());

		for (JayBotItemInfo videoInfo : videoList) {
			// 写入视频基本信息
			{
				StrBuilder outLine = new StrBuilder();

				outLine.append(videoInfo.videoNum).append('\t');
				outLine.append(videoInfo.createTime).append('\t');
				outLine.append(videoInfo.score).append('\t');
				outLine.append(videoInfo.name);

				String str = outLine.toString();
				logger.info("video={}", str);
				outLineList.add(str);
			}

			String keyword = StringUtils.stripEnd(Base64.getEncoder().encodeToString(videoInfo.videoNum.getBytes(StandardCharsets.UTF_8)), "=");
			String urlTemplate = StrUtils.generateMessage("search?word={}&sort=time&p=", keyword);

			for (int i = 1; i <= maxPage; i++) {
				List<CilimaoLinkedInfo> linkedInfoList = cilimaoApp.getLinkedInfoList(urlTemplate, i);
				if (linkedInfoList.isEmpty()) {
					logger.info("page={}, linkedInfoList is empty", i);
					break;
				}
				logger.info("page={}, linkedInfoList = {}", i, JSON.toJSONString(linkedInfoList));

				for (CilimaoLinkedInfo linkedInfo : linkedInfoList) {
					StrBuilder outLine = new StrBuilder();

					if (limitMp4Map.containsKey(linkedInfo.name)) {
						outLine.append("exists!!!").append('\t');
					}
					outLine.append(linkedInfo.name).append('\t');

					outLine.append(linkedInfo.url).append('\t');
					outLine.append(linkedInfo.createTime).append('\t');

					String str = outLine.toString();
					logger.info("linked={}", str);
					outLineList.add(str);

				}

				sleepRandom(false);
			}
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		final String outFn = StrUtils.generateMessage("{}_result_{}.txt",
				actorName, dateFormat.format(new Date()));

		FileUtils.writeLines(new File(resultFolder, outFn), "utf-8", outLineList);
	}

	protected Map<String, String> loadFile(String loadFileTemplate) throws Exception {
		String fileName = StrUtils.generateMessage(loadFileTemplate, loadFileDate);
		final File loadFile = new File(folder, fileName);
		if (!loadFile.exists()) {
			throw new RuntimeException("loadFile not exists");
		}

		List<String> lineList = FileUtils.readLines(loadFile, "utf-8");
		Map<String, String> limitGirlMap = Maps.newHashMapWithExpectedSize(lineList.size());
		for (String girl : lineList) {
			String[] split = StringUtils.split(girl, '\t');
			String key = split[0];
			String value = split[1];
			putMap(limitGirlMap, key, value);

			// 多名字识别
			if (value.length() <= 3) {
				// 确保value是等级，而不是子目录
				if (key.endsWith("）") && key.contains("（")) {
					String current = StringUtils.substringBefore(key, "（");
					putMap(limitGirlMap, current, value);

					String special = StringUtils.substringBetween(key, "（", "）");
					putMap(limitGirlMap, special, value);

					if (special.contains("、")) {
						String[] specialArray = StringUtils.split(special, '、');
						for (String specialName : specialArray) {
							putMap(limitGirlMap, specialName, value);
						}
					}
				}
			}

		}

		return limitGirlMap;
	}

	private void putMap(Map<String, String> limitGirlMap, String key, String value) {
		if (limitGirlMap.containsKey(key)) {
			logger.warn("girl={} 重复了!!! value={}", key, value);
			return;
		}
		limitGirlMap.put(key, value);
	}

	private List<JayBotItemInfo> searchV2(JayBot jayBot, String name, List<String> outLineList,
										  int page, CilimaoSearchTypeEnum searchTypeEnum) throws Exception {
		Exception ex = null;
		for (int i = 0; i < 5; i++) {

			try {
				return jayBot.searchV2(name);
			} catch (Exception e) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				final String outFn = StrUtils.generateMessage("{}_error_{}.txt",
						searchTypeEnum.name(), dateFormat.format(new Date()));
				outLineList.add(StrUtils.generateMessage("last page={}", page));
				FileUtils.writeLines(new File(resultFolder, outFn), "utf-8", outLineList);

				for (int j = 0; j < 20; j++) {
					sleepRandom(true);
				}

				ex = e;
			}
		}
		throw ex;
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
