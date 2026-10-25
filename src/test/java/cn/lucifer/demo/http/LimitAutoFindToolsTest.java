package cn.lucifer.demo.http;

import cn.lucifer.demo.http.dict.CilimaoSearchTypeEnum;
import cn.lucifer.demo.http.domain.CilimaoLinkedInfo;
import cn.lucifer.demo.http.domain.JayBotItemInfo;
import cn.lucifer.demo.http.domain.LoadFileInfo;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import static org.junit.Assert.*;

/**
 * LimitAutoFindTools 手动调试入口。
 * <p>
 * 构造参数（4 参版）：startPage 起始页(从1开始，仅测 loadFile 时传 0 即可)、startVideo 起始番号(可 null)、
 * loadFileDate 索引文件日期(如 "20260419"，对应 limit_mp4_20260419.txt)、resultFolder 结果目录(仅读索引可传 null)。
 * <p>
 * 需要真实跑任务时用 5 参版（多一个 javbot3Cookie，可通过它调用 autoFind / autoFindByAuthor）。
 */
public class LimitAutoFindToolsTest {

	/**
	 * 读取 M:\limit\aaa\limit_mp4_20261025.txt 并打印，用于校验索引解析是否正确。
	 */
	@Test
	public void loadFile() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null,  "20261025", null);
		Map<String, LoadFileInfo> mp4 = tools.loadFile("limit_mp4_{}.txt");
		System.out.println(JSON.toJSONString(mp4));
	}

	/**
	 * 校验黑名单已从文件索引中分离：集合非空、裸番号不再出现在文件索引里、番号比较忽略大小写
	 */
	@Test
	public void blacklist() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null, "20261025", null);
		Map<String, LoadFileInfo> mp4 = tools.loadLimitMp4Map();

		System.out.println("blacklist size=" + tools.blacklistVideoNum.size());
		assertFalse(tools.blacklistVideoNum.isEmpty());
		assertFalse(mp4.containsKey("300MIUM-1261"));

		assertTrue(tools.isBlacklist("300MIUM-1261"));
		assertTrue(tools.isBlacklist("300mium-1261"));
		assertFalse(tools.isBlacklist("300MIUM-1261-U"));
	}

	/**
	 * 校验黑名单只追加一行蓝色标记，未命中时不追加
	 */
	@Test
	public void blacklistLine() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null, "20261025", null);
		tools.loadLimitMp4Map();

		List<String> outLineList = Lists.newArrayList();
		JayBotItemInfo videoInfo = new JayBotItemInfo();
		videoInfo.videoNum = "300MIUM-1261";
		assertTrue(tools.appendBlacklistIfNeeded(outLineList, videoInfo));
		assertEquals(1, outLineList.size());
		assertEquals("<div class=\"blue_b\">blacklist</div>", outLineList.get(0));

		videoInfo.videoNum = "SONE-009";
		assertFalse(tools.appendBlacklistIfNeeded(outLineList, videoInfo));
		assertEquals(1, outLineList.size());
	}

	/**
	 * 校验候选标题：原样、原样+后缀、去最后一个 @ 前缀、去前缀+后缀
	 */
	@Test
	public void titleCandidates() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null, "20261025", null);

		List<String> candidates = tools.buildTitleCandidates("第一會所新片@SIS001@MRHP-004-U", ".mp4");
		assertEquals(4, candidates.size());
		assertEquals("第一會所新片@SIS001@MRHP-004-U", candidates.get(0));
		assertEquals("MRHP-004-U", candidates.get(2));

		assertEquals(2, tools.buildTitleCandidates("MRHP-004-U", ".mp4").size());
	}

	/**
	 * 校验按候选命中本地索引：带前缀与不带前缀都能命中，带站点前缀的索引键保持可命中
	 */
	@Test
	public void findByCandidates() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null, "20261025", null);
		Map<String, LoadFileInfo> mp4 = tools.loadLimitMp4Map();

		assertNotNull(tools.findLocalMp4(mp4, "MRHP-004-U", CilimaoSearchTypeEnum.base64));
		assertNotNull(tools.findLocalMp4(mp4, "第一會所新片@SIS001@MRHP-004-U", CilimaoSearchTypeEnum.base64));
		assertNotNull(tools.findLocalMp4(mp4, "hhd800.com@SONE-009", CilimaoSearchTypeEnum.base64));
	}

	/**
	 * 校验大小门槛：差值不超过0.1不算冲突，缺数据或非GB视为不可比
	 */
	@Test
	public void sizeConflict() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null, "20261025", null);
		Map<String, LoadFileInfo> mp4 = tools.loadLimitMp4Map();

		LoadFileInfo matchedInfo = tools.findLocalMp4(mp4, "MRHP-004-U", CilimaoSearchTypeEnum.base64);
		assertNotNull(matchedInfo);
		assertFalse(tools.isSizeConflict(matchedInfo, "5.95 GB"));
		assertTrue(tools.isSizeConflict(matchedInfo, "6.40 GB"));
		assertFalse(tools.isSizeConflict(matchedInfo, "850 MB"));

		LoadFileInfo noSize = new LoadFileInfo("unknowns", null);
		assertFalse(tools.isSizeConflict(noSize, "5.95 GB"));
	}

	/**
	 * 校验修饰标记：原始文件名与别名命中的标记一致
	 */
	@Test
	public void marksOnAlias() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null, "20261025", null);
		Map<String, LoadFileInfo> mp4 = tools.loadLimitMp4Map();

		LoadFileInfo byAlias = tools.findLocalMp4(mp4, "MRHP-004-U", CilimaoSearchTypeEnum.base64);
		assertNotNull(byAlias);
		assertEquals("no_mark", byAlias.marks);

		LoadFileInfo byOrigin = tools.findLocalMp4(mp4, "MRHP-004-U_no_mark.mp4", CilimaoSearchTypeEnum.base64);
		assertNotNull(byOrigin);
		assertEquals("no_mark", byOrigin.marks);
	}

	/**
	 * 校验标记标签：无标记不输出、标题已含则不重复、多标记按顺序拼接
	 */
	@Test
	public void marksSpan() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null, "20261025", null);

		LoadFileInfo info = new LoadFileInfo("篠田ゆう", "5.93", "no_mark");
		assertEquals("<span class=\"green_b\">no_mark</span>", tools.buildMarksSpan(info, "MRHP-004-U"));
		assertEquals(StringUtils.EMPTY, tools.buildMarksSpan(info, "MRHP-004-U_no_mark.mp4"));
		assertEquals(StringUtils.EMPTY, tools.buildMarksSpan(new LoadFileInfo("篠田ゆう", null), "MRHP-004-U"));

		LoadFileInfo multi = new LoadFileInfo("篠田ゆう", "5.93", "nice/no_mark");
		assertEquals("<span class=\"green_b\">nice</span><span class=\"green_b\">no_mark</span>",
				tools.buildMarksSpan(multi, "MRHP-004-U"));
	}

	/**
	 * 校验后缀转换命中的条目按枚举配置的容差比较，未配置的条目仍用默认 0.1
	 */
	@Test
	public void conversionHitUsesConfiguredTolerance() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null, "20261025", null);
		Map<String, LoadFileInfo> mp4 = tools.loadLimitMp4Map();

		assertNull(tools.findLocalMp4(mp4, "SONE-626-uncensored-HD", CilimaoSearchTypeEnum.base64));

		// 3.04 本地，网页 3.17，差值 0.13：默认容差不过，配置的 0.3 放行
		LoadFileInfo matchedInfo = tools.findExisting(mp4, "SONE-626-uncensored-HD", "3.17 GB", CilimaoSearchTypeEnum.base64);
		assertNotNull(matchedInfo);
		assertEquals("3.04", matchedInfo.fileSizeGB);
		assertEquals(Double.valueOf(0.3), matchedInfo.sizeTolerance);

		// 差值过大仍然否决
		assertNull(tools.findExisting(mp4, "SONE-626-uncensored-HD", "5.00 GB", CilimaoSearchTypeEnum.base64));

		// 普通条目没有配置容差，仍用默认 0.1
		LoadFileInfo normalInfo = tools.findLocalMp4(mp4, "MRHP-004-U", CilimaoSearchTypeEnum.base64);
		assertNotNull(normalInfo);
		assertNull(normalInfo.sizeTolerance);
		assertNull(tools.findExisting(mp4, "MRHP-004-U", "6.40 GB", CilimaoSearchTypeEnum.base64));
		assertNotNull(tools.findExisting(mp4, "MRHP-004-U", "5.95 GB", CilimaoSearchTypeEnum.base64));
	}

	/**
	 * 校验标记只在判定为已存在时出现，且样式表包含 green_b 定义
	 */
	@Test
	public void marksInLine() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null, "20261025", null);
		Map<String, LoadFileInfo> mp4 = tools.loadLimitMp4Map();
		List<String> blueSuffixList = Lists.newArrayList("-U", "-UC");

		CilimaoLinkedInfo linkedInfo = new CilimaoLinkedInfo();
		linkedInfo.name = "MRHP-004-U";
		linkedInfo.url = "https://clm15.vip//information/test";
		linkedInfo.createTime = "2025-12-17";
		linkedInfo.fileSize = "5.95 GB";

		String line = tools.buildLinkedInfoLine(linkedInfo, mp4, CilimaoSearchTypeEnum.base64, blueSuffixList);
		assertTrue(line.contains("exists!!!"));
		assertTrue(line.contains("<span class=\"green_b\">no_mark</span>"));

		linkedInfo.fileSize = "6.40 GB";
		line = tools.buildLinkedInfoLine(linkedInfo, mp4, CilimaoSearchTypeEnum.base64, blueSuffixList);
		assertFalse(line.contains("exists!!!"));
		assertFalse(line.contains("green_b"));

		List<String> headLines = Lists.newArrayList();
		tools.initHtmlHead(headLines, new Stack<>());
		assertTrue(headLines.contains(".green_b{color:green;font-weight:bolder;}"));
	}
}