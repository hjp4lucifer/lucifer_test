package cn.lucifer.demo.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class LimitAutoFindToolsTest {

	@Test
	public void loadFile() throws Exception{
		LimitAutoFindTools tools = new LimitAutoFindTools(0, null,  "20260306", null);
		Map<String, String> mp4 = tools.loadFile("limit_mp4_{}.txt");
		System.out.println(JSON.toJSONString(mp4));
	}
}