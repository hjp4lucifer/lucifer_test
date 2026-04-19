package cn.lucifer.demo.http;

import cn.lucifer.demo.http.dict.CilimaoSearchTypeEnum;
import cn.lucifer.demo.http.domain.CilimaoLinkedInfo;
import cn.lucifer.demo.http.domain.JayBotActorPageResult;
import cn.lucifer.demo.http.domain.JayBotItemInfo;
import cn.lucifer.demo.http.domain.LoadFileInfo;
import cn.lucifer.http.HttpClientException;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class LimitAutoFindTools {

	private static final Logger logger = LoggerFactory.getLogger(LimitAutoFindTools.class);

	final File folder = new File("M:\\limit\\aaa");

	private final int startPage;
	private final String startVideo;
	private final String loadFileDate;
	private final File resultFolder;
	private BasicCookieStore jayBotCookieStore;

	protected LimitAutoFindTools(int startPage, String startVideo, String loadFileDate, File resultFolder) {
		this.startPage = startPage;
		this.startVideo = startVideo;
		this.loadFileDate = loadFileDate;
		this.resultFolder = resultFolder;
	}

	public LimitAutoFindTools(int startPage, String startVideo, String javbot3Cookie, String loadFileDate, File resultFolder) {
		this(startPage, startVideo, loadFileDate, resultFolder);
		BasicCookieStore cookieStore = CookiesUtils.getCookieStore("javbot3.top", javbot3Cookie);
		String javbot3CookieToken = CookiesUtils.getByName(cookieStore, "csrf_cookie");
		if (null == javbot3CookieToken) {
			throw new IllegalArgumentException("javbot3_cookie_token is null");
		}
		this.jayBotCookieStore = cookieStore;
	}


	public void autoFind(CilimaoSearchTypeEnum searchTypeEnum, String loadEndTime,
						 int maxPage, File oldFile) throws Exception {
		final Map<String, LoadFileInfo> limitGirlMap = loadFile("limit_girl_{}.txt");
		final Map<String, LoadFileInfo> limitMp4Map = loadFile("limit_mp4_{}.txt");

		File[] mp4FileArray = folder.listFiles(f -> f.getName().endsWith(".mp4"));
		if (null != mp4FileArray) {
			for (File mp4File : mp4FileArray) {
				String name = mp4File.getName();
				limitMp4Map.put(name, new LoadFileInfo("unknowns", null));
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

					String girlRating = limitGirlMap.containsKey(videoInfo.actress) ? limitGirlMap.get(videoInfo.actress).parentName : null;
					// 评分
					outLine.append(StringUtils.defaultString(girlRating, "unknowns")).append('\t');
					if (StringUtils.isBlank(videoInfo.actress)) {
						logger.info("{} 识别不出 actress 名字，查详情页!!!", linkedInfo.name);
						JayBotItemInfo detail = jayBot.getDetail(videoInfo.detailUrl);
						outLine.append(detail.actress).append('\t');
					} else {
						outLine.append(videoInfo.actress).append('\t');
					}
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
		final Map<String, LoadFileInfo> limitMp4Map = loadFile("limit_mp4_{}.txt");

		File[] mp4FileArray = folder.listFiles(f -> f.getName().endsWith(".mp4"));
		if (null != mp4FileArray) {
			for (File mp4File : mp4FileArray) {
				String name = mp4File.getName();
				limitMp4Map.put(name, new LoadFileInfo("unknowns", null));
			}
		}

		Stack<String> htmlStack = new Stack<>();
		final List<String> outLineList = Lists.newArrayList();
		outLineList.add("<html>");
		htmlStack.push("</html>");
		outLineList.add("<head>");
		htmlStack.push("</head>");
		// 样式
		outLineList.add("<style type=\"text/css\">");
		outLineList.add("h3 span{padding:0px 8px}");
		outLineList.add("li {padding:3px}");
		outLineList.add("li span{padding:0px 5px}");
		outLineList.add(".red_b{color:red;font-weight:bolder;}");
		outLineList.add(".blue_b{color:blue;font-weight:bolder;}");
		outLineList.add("</style>");

		JayBot jayBot = new JayBot(jayBotCookieStore);
		String actorName = StringUtils.EMPTY;

		// 手动控制分页
		List<JayBotItemInfo> videoList = Lists.newArrayList();
		int currentPage = 1;
		while (currentPage <= maxPage) {
			JayBotActorPageResult pageResult = jayBot.getByActor(actorCode, currentPage);
			if (1 == currentPage) {
				actorName = pageResult.actorName;
				outLineList.add(StrUtils.generateMessage("<title>{}</title>", actorName));
				outLineList.add(htmlStack.pop());

				outLineList.add("<body>");
				htmlStack.push("</body>");
				outLineList.add(StrUtils.generateMessage("<h2>{}</h2>", actorName));

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

		final List<String> buleSuffixList = Lists.newArrayList("-UC", "-U", CilimaoSearchTypeEnum.uncensored_HD.getSuffix());

		for (JayBotItemInfo videoInfo : videoList) {
			// 写入视频基本信息
			{
				StrBuilder outLine = new StrBuilder("\n");

				outLine.append(StrUtils.generateMessage("<span style=\"color:blue\">{}</span>", videoInfo.videoNum)).append('\t');
				outLine.append(StrUtils.generateMessage("<span>{}</span>", videoInfo.createTime));
				outLine.append(StrUtils.generateMessage("<span>{}</span>", videoInfo.score));
				outLine.append(StrUtils.generateMessage("<span>{}</span>", videoInfo.name));

				String str = outLine.toString();
				logger.info("video={}", JSON.toJSONString(videoInfo));
				outLineList.add(StrUtils.generateMessage("<h3>{}</h3>", str));
			}

			outLineList.add("<ul>");
			htmlStack.push("</ul>");

			String keyword = StringUtils.stripEnd(Base64.getEncoder().encodeToString(videoInfo.videoNum.getBytes(StandardCharsets.UTF_8)), "=");
			String urlTemplate = StrUtils.generateMessage("search?word={}&sort=time&p=", keyword);

			for (int i = 1; i <= maxPage; i++) {
				List<CilimaoLinkedInfo> linkedInfoList = null;
				for (int retry = 0; retry < 3; retry++) {
					try {
						linkedInfoList = cilimaoApp.getLinkedInfoList(urlTemplate, i);
						break;
					} catch (Exception e) {
						if (retry < 2) {
							logger.error("retry={}", retry, e);
							sleepRandom(true);
							continue;
						}
						outLineList.add(StrUtils.generateMessage("<li class=\"red_b\">page={} is error</li>", i));
					}
				}
				if (linkedInfoList.isEmpty()) {
					logger.info("page={}, linkedInfoList is empty", i);
					break;
				}
				logger.info("page={}, linkedInfoList = {}", i, JSON.toJSONString(linkedInfoList));

				for (CilimaoLinkedInfo linkedInfo : linkedInfoList) {
					StrBuilder outLine = new StrBuilder();

					boolean exists = false;
					String linkedTitle = linkedInfo.name;
					if (limitMp4Map.containsKey(linkedTitle)) {
						exists = true;
					} else if (linkedTitle.endsWith(CilimaoSearchTypeEnum.uncensored_HD.getSuffix())) {
						String mp4Name = StringUtils.removeEnd(linkedTitle, CilimaoSearchTypeEnum.uncensored_HD.getSuffix());
						mp4Name += CilimaoSearchTypeEnum.uncensored_HD.getToMp4Suffix();
						exists = limitMp4Map.containsKey(mp4Name);
					} else if (StringUtils.isNotEmpty(searchTypeEnum.getToMp4Suffix())) {
						String mp4Name = linkedTitle + searchTypeEnum.getToMp4Suffix();
						exists = limitMp4Map.containsKey(mp4Name);
					}
					if (exists) {
						outLine.append(StrUtils.generateMessage("<span class=\"red_b\">{}</span>", "exists!!!"));
					}

					boolean blueTitle = buleSuffixList.stream().anyMatch(linkedTitle::endsWith);
					if (blueTitle) {
						outLine.append(StrUtils.generateMessage("<span class=\"blue_b\">{}</span>", linkedTitle));
					} else {
						outLine.append(StrUtils.generateMessage("<span>{}</span>", linkedTitle));
					}
					outLine.append(StrUtils.generateMessage("<span>{}</span>", linkedInfo.fileSize));
					outLine.append(StrUtils.generateMessage("<span>{}</span>", linkedInfo.createTime));

					outLine.append(StrUtils.generateMessage("<a href=\"{}\" target=\"_black\">{}</a>", linkedInfo.url, linkedInfo.url));

					String str = outLine.toString();
					logger.info("linkedInfo={}", JSON.toJSONString(linkedInfo));
					outLineList.add(StrUtils.generateMessage("<li>{}</li>", str));

				}

				sleepRandom(false);
			}
			outLineList.add(htmlStack.pop());
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		final String outFn = StrUtils.generateMessage("{}_{}.htm",
				StringUtils.remove(actorName, "演員 "), dateFormat.format(new Date()));

		// 补全html格式
		while (!htmlStack.isEmpty()) {
			outLineList.add(htmlStack.pop());
		}

		FileUtils.writeLines(new File(resultFolder, outFn), "utf-8", outLineList);
	}

	protected Map<String, LoadFileInfo> loadFile(String loadFileTemplate) throws Exception {
		// 使用日期变量替换模板中的占位符，生成实际文件名
		String fileName = StrUtils.generateMessage(loadFileTemplate, loadFileDate);
		final File loadFile = new File(folder, fileName);
		if (!loadFile.exists()) {
			throw new RuntimeException("loadFile not exists");
		}

		// 按行读取文件内容
		List<String> lineList = FileUtils.readLines(loadFile, "utf-8");
		// 预分配Map容量，避免频繁扩容
		Map<String, LoadFileInfo> limitMap = Maps.newLinkedHashMapWithExpectedSize(lineList.size());
		for (String line : lineList) {
			// 按Tab分隔每行，第一列为key，第二列为parentName或fileSizeGB，第三列为parentName（当有fileSizeGB时）
			String[] split = StringUtils.split(line, '\t');
			String key = split[0];
			String parentName;
			String fileSizeGB = null;
			if (split.length >= 3) {
				// 格式: key\tfileSizeGB\tparentName
				fileSizeGB = split[1];
				parentName = split[2];
			} else {
				// 格式: key\tparentName
				parentName = split[1];
			}
			putMap(limitMap, key, new LoadFileInfo(parentName, fileSizeGB));

			// 若key以"_nice.mp4"结尾，仅移除"_nice"后也作为key建立映射（保留.mp4）
			if (key.endsWith("_nice.mp4")) {
				String keyWithoutNice = StringUtils.removeEnd(key, "_nice.mp4") + ".mp4";
				putMap(limitMap, keyWithoutNice, new LoadFileInfo(parentName, fileSizeGB));
			}

			// 多名字识别
			if (parentName.length() <= 3) {
				// 确保parentName是等级，而不是子目录
				if (key.endsWith("）") && key.contains("（")) {
					// 提取括号前的主名，建立映射
					String current = StringUtils.substringBefore(key, "（");
					putMap(limitMap, current, new LoadFileInfo(parentName, fileSizeGB));

					// 提取括号内的别名，建立映射
					String special = StringUtils.substringBetween(key, "（", "）");
					putMap(limitMap, special, new LoadFileInfo(parentName, fileSizeGB));

					// 若别名包含顿号，则拆分为多个名字分别映射
					if (special.contains("、")) {
						String[] specialArray = StringUtils.split(special, '、');
						for (String specialName : specialArray) {
							putMap(limitMap, specialName, new LoadFileInfo(parentName, fileSizeGB));
						}
					}
				}
			}

		}

		return limitMap;
	}

	private void putMap(Map<String, LoadFileInfo> limitMap, String key, LoadFileInfo info) {
		if (limitMap.containsKey(key)) {
			logger.warn("girl={} 重复了!!! parentName={}", key, info.parentName);
			return;
		}
		limitMap.put(key, info);
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
