package cn.lucifer.demo.http;

import cn.lucifer.demo.http.domain.LoadFileInfo;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Map;

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
	 * 读取 M:\limit\aaa\limit_mp4_20260419.txt 并打印，用于校验索引解析是否正确。
	 */
	@Test
	public void loadFile() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null,  "20260419", null);
		Map<String, LoadFileInfo> mp4 = tools.loadFile("limit_mp4_{}.txt");
		System.out.println(JSON.toJSONString(mp4));
	}
}