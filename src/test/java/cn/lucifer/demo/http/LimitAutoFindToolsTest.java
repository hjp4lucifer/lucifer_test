package cn.lucifer.demo.http;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static cn.lucifer.demo.http.AutoFindToolsTest.*;

public class LimitAutoFindToolsTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void loadFile() throws Exception {
		LimitAutoFindTools tools = new LimitAutoFindTools(1, startVideo, javbot3_cookie_token, load_file_date, result_folder);
		Map<String, String> limitGirlMap = tools.loadFile("M:\\limit\\aaa\\limit_girl_{}.txt");
		logger.info("limitGirlMap={}", JSON.toJSONString(limitGirlMap));
	}
}