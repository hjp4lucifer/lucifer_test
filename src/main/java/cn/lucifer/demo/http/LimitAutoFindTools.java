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
		BasicCookieStore cookieStore = CookiesUtils.getCookieStore(JayBot.COOKIE_DOMAIN, javbot3Cookie);
		String javbot3CookieToken = CookiesUtils.getByName(cookieStore, "csrf_cookie");
		if (null == javbot3CookieToken) {
			throw new IllegalArgumentException("javbot3_cookie_token is null");
		}
		this.jayBotCookieStore = cookieStore;
	}


	public void autoFind(CilimaoSearchTypeEnum searchTypeEnum, String loadEndTime,
						 int maxPage, File oldFile) throws Exception {
		final Map<String, LoadFileInfo> limitGirlMap = loadFile("limit_girl_{}.txt");
		final Map<String, LoadFileInfo> limitMp4Map = loadLimitMp4Map();

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
		// 加载本地已有的mp4索引
		final Map<String, LoadFileInfo> limitMp4Map = loadLimitMp4Map();

		// 初始化HTML输出结构和样式
		Stack<String> htmlStack = new Stack<>();
		final List<String> outLineList = Lists.newArrayList();
		initHtmlHead(outLineList, htmlStack);

		// 分页获取演员的所有视频列表
		JayBot jayBot = new JayBot(jayBotCookieStore);
		String actorName = StringUtils.EMPTY;
		List<JayBotItemInfo> videoList = fetchActorVideoList(jayBot, actorCode, maxPage, outLineList, htmlStack);
		actorName = videoList.isEmpty() ? actorName : getActorNameFromFirstPage(jayBot, actorCode);
		logger.info("videoList total = {}", JSON.toJSONString(videoList));

		// 遍历每个视频，在磁力搜索站点查找对应的资源链接
		CilimaoSearchTypeEnum searchTypeEnum = CilimaoSearchTypeEnum.base64;
		CilimaoApp cilimaoApp = new CilimaoApp(searchTypeEnum, new BasicCookieStore());

		for (JayBotItemInfo videoInfo : videoList) {
			// 写入视频基本信息（标题行）
			appendVideoInfoHeader(outLineList, videoInfo);

			outLineList.add("<ul>");
			htmlStack.push("</ul>");

			// 构建搜索关键词和URL模板
			String keyword = StringUtils.stripEnd(Base64.getEncoder().encodeToString(videoInfo.videoNum.getBytes(StandardCharsets.UTF_8)), "=");
			String urlTemplate = StrUtils.generateMessage("search?word={}&sort=time&p=", keyword);

			// 分页搜索该视频的磁力链接
			searchLinkedInfoByVideo(cilimaoApp, searchTypeEnum, limitMp4Map, urlTemplate, maxPage, outLineList);

			outLineList.add(htmlStack.pop());
		}

		// 输出结果文件
		writeActorResultHtml(actorName, outLineList, htmlStack);
	}

	/**
	 * 加载本地mp4索引，并合并当前文件夹下的mp4文件
	 */
	private Map<String, LoadFileInfo> loadLimitMp4Map() throws Exception {
		final Map<String, LoadFileInfo> limitMp4Map = loadFile("limit_mp4_{}.txt");
		// 将当前文件夹下的mp4文件也加入索引（标记为unknowns）
		File[] mp4FileArray = folder.listFiles(f -> f.getName().endsWith(".mp4"));
		if (null != mp4FileArray) {
			for (File mp4File : mp4FileArray) {
				String name = mp4File.getName();
				limitMp4Map.put(name, new LoadFileInfo("unknowns", null));
			}
		}
		return limitMp4Map;
	}

	/**
	 * 初始化HTML文档的头部和样式
	 */
	private void initHtmlHead(List<String> outLineList, Stack<String> htmlStack) {
		outLineList.add("<html>");
		htmlStack.push("</html>");
		outLineList.add("<head>");
		htmlStack.push("</head>");
		// 样式定义
		outLineList.add("<style type=\"text/css\">");
		outLineList.add("h3 span{padding:0px 8px}");
		outLineList.add("li {padding:3px}");
		outLineList.add("li span{padding:0px 5px}");
		outLineList.add(".red_b{color:red;font-weight:bolder;}");
		outLineList.add(".blue_b{color:blue;font-weight:bolder;}");
		outLineList.add("</style>");
	}

	/**
	 * 从第一页获取演员名称
	 */
	private String getActorNameFromFirstPage(JayBot jayBot, String actorCode) throws Exception {
		JayBotActorPageResult firstPage = jayBot.getByActor(actorCode, 1);
		return firstPage.actorName;
	}

	/**
	 * 分页获取演员的所有视频列表
	 */
	private List<JayBotItemInfo> fetchActorVideoList(JayBot jayBot, String actorCode, int maxPage,
													 List<String> outLineList, Stack<String> htmlStack) throws Exception {
		List<JayBotItemInfo> videoList = Lists.newArrayList();
		int currentPage = 1;
		while (currentPage <= maxPage) {
			JayBotActorPageResult pageResult = jayBot.getByActor(actorCode, currentPage);
			// 首页时写入标题和body开始标签
			if (1 == currentPage) {
				String actorName = pageResult.actorName;
				outLineList.add(StrUtils.generateMessage("<title>{}</title>", actorName));
				outLineList.add(htmlStack.pop()); // </head>
				outLineList.add("<body>");
				htmlStack.push("</body>");
				outLineList.add(StrUtils.generateMessage("<h2>{}</h2>", actorName));
			}
			// 无更多数据则退出
			if (pageResult.items == null || pageResult.items.isEmpty()) {
				break;
			}
			videoList.addAll(pageResult.items);
			logger.info(StrUtils.generateMessage("videoList page={}, size={}, total={}", currentPage, pageResult.items.size(), videoList.size()));

			// 无下一页则退出
			if (pageResult.nextPage <= 0) {
				break;
			}
			currentPage++;
			sleepRandom(false);
		}
		return videoList;
	}

	/**
	 * 写入视频基本信息（标题行），包含番号、时间、评分、名称
	 */
	private void appendVideoInfoHeader(List<String> outLineList, JayBotItemInfo videoInfo) {
		StrBuilder outLine = new StrBuilder("\n");
		outLine.append(StrUtils.generateMessage("<span style=\"color:blue\">{}</span>", videoInfo.videoNum)).append('\t');
		outLine.append(StrUtils.generateMessage("<span>{}</span>", videoInfo.createTime));
		outLine.append(StrUtils.generateMessage("<span>{}</span>", videoInfo.score));
		outLine.append(StrUtils.generateMessage("<span>{}</span>", videoInfo.name));
		String str = outLine.toString();
		logger.info("video={}", JSON.toJSONString(videoInfo));
		outLineList.add(StrUtils.generateMessage("<h3>{}</h3>", str));
	}

	/**
	 * 分页搜索视频的磁力链接信息，逐条匹配并写入结果
	 */
	private void searchLinkedInfoByVideo(CilimaoApp cilimaoApp, CilimaoSearchTypeEnum searchTypeEnum,
										 Map<String, LoadFileInfo> limitMp4Map,
										 String urlTemplate, int maxPage,
										 List<String> outLineList) throws Exception {
		final List<String> blueSuffixList = Lists.newArrayList("-UC", "-U", CilimaoSearchTypeEnum.uncensored_HD.getSuffix());

		for (int i = 1; i <= maxPage; i++) {
			// 带重试的搜索请求
			List<CilimaoLinkedInfo> linkedInfoList = retrySearchLinkedInfo(cilimaoApp, urlTemplate, i, outLineList);
			if (linkedInfoList.isEmpty()) {
				logger.info("page={}, linkedInfoList is empty", i);
				break;
			}
			logger.info("page={}, linkedInfoList = {}", i, JSON.toJSONString(linkedInfoList));

			// 遍历搜索结果，逐条匹配并生成HTML行
			for (CilimaoLinkedInfo linkedInfo : linkedInfoList) {
				String htmlLine = buildLinkedInfoLine(linkedInfo, limitMp4Map, searchTypeEnum, blueSuffixList);
				logger.info("linkedInfo={}", JSON.toJSONString(linkedInfo));
				outLineList.add(StrUtils.generateMessage("<li>{}</li>", htmlLine));
			}

			sleepRandom(false);
		}
	}

	/**
	 * 带重试的搜索请求，最多重试3次
	 */
	private List<CilimaoLinkedInfo> retrySearchLinkedInfo(CilimaoApp cilimaoApp, String urlTemplate,
														  int page, List<String> outLineList) {
		for (int retry = 0; retry < 3; retry++) {
			try {
				return cilimaoApp.getLinkedInfoList(urlTemplate, page);
			} catch (Exception e) {
				if (retry < 2) {
					logger.error("retry={}", retry, e);
					sleepRandom(true);
					continue;
				}
				outLineList.add(StrUtils.generateMessage("<li class=\"red_b\">page={} is error</li>", page));
			}
		}
		return Lists.newArrayList();
	}

	/**
	 * 构建单条磁力链接信息的HTML行
	 * 匹配逻辑：标题匹配本地已有mp4，文件大小匹配则高亮（blue_b）
	 */
	private String buildLinkedInfoLine(CilimaoLinkedInfo linkedInfo, Map<String, LoadFileInfo> limitMp4Map,
									   CilimaoSearchTypeEnum searchTypeEnum, List<String> blueSuffixList) {
		StrBuilder outLine = new StrBuilder();
		String linkedTitle = linkedInfo.name;

		// 检查本地是否已存在该资源
		boolean exists = false;
		LoadFileInfo matchedInfo = null;
		if (limitMp4Map.containsKey(linkedTitle)) {
			exists = true;
			matchedInfo = limitMp4Map.get(linkedTitle);
		} else if (linkedTitle.endsWith(CilimaoSearchTypeEnum.uncensored_HD.getSuffix())) {
			// 无码高清后缀转换
			String mp4Name = StringUtils.removeEnd(linkedTitle, CilimaoSearchTypeEnum.uncensored_HD.getSuffix());
			mp4Name += CilimaoSearchTypeEnum.uncensored_HD.getToMp4Suffix();
			if (limitMp4Map.containsKey(mp4Name)) {
				exists = true;
				matchedInfo = limitMp4Map.get(mp4Name);
			}
		} else if (StringUtils.isNotEmpty(searchTypeEnum.getToMp4Suffix())) {
			// 根据搜索类型后缀转换
			String mp4Name = linkedTitle + searchTypeEnum.getToMp4Suffix();
			if (limitMp4Map.containsKey(mp4Name)) {
				exists = true;
				matchedInfo = limitMp4Map.get(mp4Name);
			}
		}

		// 已存在的资源标记为红色
		if (exists) {
			outLine.append(StrUtils.generateMessage("<span class=\"red_b\">{}</span>", "exists!!!"));
		}

		// 标题：特定后缀（无码等）高亮为蓝色
		boolean blueTitle = blueSuffixList.stream().anyMatch(linkedTitle::endsWith);
		if (blueTitle) {
			outLine.append(StrUtils.generateMessage("<span class=\"blue_b\">{}</span>", linkedTitle));
		} else {
			outLine.append(StrUtils.generateMessage("<span>{}</span>", linkedTitle));
		}

		// 文件大小：与本地记录匹配时高亮为蓝色（仅GB单位参与匹配）
		boolean fileSizeMatched = checkFileSizeMatched(exists, matchedInfo, linkedInfo.fileSize);
		if (fileSizeMatched) {
			outLine.append(StrUtils.generateMessage("<span class=\"blue_b\">{}</span>", linkedInfo.fileSize));
		} else {
			outLine.append(StrUtils.generateMessage("<span>{}</span>", linkedInfo.fileSize));
		}

		// 创建时间和链接
		outLine.append(StrUtils.generateMessage("<span>{}</span>", linkedInfo.createTime));
		outLine.append(StrUtils.generateMessage("<a href=\"{}\" target=\"_black\">{}</a>", linkedInfo.url, linkedInfo.url));

		return outLine.toString();
	}

	/**
	 * 检查文件大小是否与本地记录匹配（仅GB单位参与匹配，差值<0.005视为匹配）
	 */
	private boolean checkFileSizeMatched(boolean exists, LoadFileInfo matchedInfo, String linkedFileSize) {
		if (!exists || matchedInfo == null || matchedInfo.fileSizeGB == null
				|| linkedFileSize == null || !linkedFileSize.endsWith(" GB")) {
			return false;
		}
		String linkedFileSizeNum = StringUtils.removeEnd(linkedFileSize, " GB");
		try {
			double linkedGB = Double.parseDouble(linkedFileSizeNum);
			double storedGB = Double.parseDouble(matchedInfo.fileSizeGB);
			return Math.abs(linkedGB - storedGB) <= 0.1;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 补全HTML格式并写入结果文件
	 */
	private void writeActorResultHtml(String actorName, List<String> outLineList, Stack<String> htmlStack) throws IOException {
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
