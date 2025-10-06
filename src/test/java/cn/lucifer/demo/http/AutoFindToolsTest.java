package cn.lucifer.demo.http;

import cn.lucifer.demo.http.domain.CilimaoLinkedInfo;
import cn.lucifer.demo.http.domain.JayBotItemInfo;
import cn.lucifer.http.HttpClientException;
import cn.lucifer.util.HttpClient5Helper;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.*;

public class AutoFindToolsTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@Test
	public void autoFind() throws Exception {
		File limitGirlFile = new File("M:\\limit\\aaa\\limit_girl_20250928.txt");
		List<String> limitGirlList = FileUtils.readLines(limitGirlFile, "utf-8");


	}

	@Test
	public void cilimaoApp() throws Exception {
		final String loadEndTime = "2025-10-04";
		CilimaoApp cilimaoApp = new CilimaoApp(loadEndTime, "mem_tip=1; PHPSESSID=tbsueeis836nivhh1nkrea9pu3");

		for (int i = 2; i <= 2; i++) {
			List<CilimaoLinkedInfo> resultList = cilimaoApp.getLinkedInfoList(i);
			logger.info("resultList = {}", JSON.toJSONString(resultList));
		}
	}


	@Test
	public void jayBot_search() throws Exception {
		final String keyword = "SONE-915";
		JayBot jayBot = new JayBot("csrf_cookie=46e6dc52549b702475553148c0424c6b;");

		List<String> urlList = jayBot.search(keyword);
		logger.info("urlList = {}", JSON.toJSONString(urlList));

		JayBotItemInfo info = jayBot.getDetail(urlList.get(0));

		logger.info("info = {}", JSON.toJSONString(info));
	}

	@Test
	public void jayBot_detail() throws Exception {
		JayBot jayBot = new JayBot("aa5cdb6ddd99b1687165092160bda265=400907d395ffae5d22351c949726e7d8; csrf_cookie=46e6dc52549b702475553148c0424c6b; _clck=snbd7f%5E2%5Efzx%5E0%5E2027; cqse=AWNUPwBjVjsALQEnV2pVZwAwVG5Sd1QjXjlRIwdyD2AEaFdkUAEGb1QzVCdQPVt%2FVDgEYl1oBj9TcQ88BTEHOlJiWjhSZFNmAzdQNQI0VjIBNlQ1AGNWNwBlATFXZ1VmAGFUZVJsVGdePVFlBzgPbwRkVzNQbQYwVDJUJ1A9W39UOARgXWoGP1NxD2YFcgdcUjFaP1IwUycDZVB%2BAn9WdAE5VHYAbFYwAGUBbldyVWIANVR6UmRUZV5tUX4HMA86BDVXJFBqBjJUdVQ%2BUHVbNlQzBGFdYAYnUyYPfAVnB3FSD1o6UjNTMANuUHkCLlZtAXFUPwBnVjAAYwFuV3JVGwBvVC5SPFQ8XjBRMQcuDzwEKVc6UH4GLlQAVGxQaFtoVG0EJl0pBiVTHQ9bBSIHMlJgWnVSZFNuAyBQWgJlVjgBNFQxAG1WIQAuAWJXZFV%2FACBUFVIlVCBeMFE1B1YPbARlV0FQNwZyVHhUMFA1WztULARiXWwGJVN7D0QFSgdXUh1aF1J4U3UDbFBkAmdWMwEiVEIAM1ZiAD0BO1d5VXYAQ1Q8UidUP14xUTUHLg8wBDVXJFBuBihUY1QwUDdbOVQsBGBdagYyU3MPXAVjB2VSMVopUj1TegM1UD4CO1Z4ATFUMwB0VjoAJQFuV2FVZQA6VHZSOVQxXi9RJAdeD2gEZFd%2BUDcGcFQ%2BVHFQf1suVDkEOF1gBjRTZA86BTsHNFJjWmlSY1NlAzVQNgJ%2FVmwBO1Q%2FAHRWdAAlATFXIlUJAGRUNVIhVDFeflFrB3IPMwQ3VzBQfAYkVGxUeA%3D%3D; _clsk=j5cf4z%5E1759732818587%5E18%5E1%5En.clarity.ms%2Fcollect");

		String url = "/item/ZxYjn";
		JayBotItemInfo info = jayBot.getDetail(url);

		logger.info("info = {}", JSON.toJSONString(info));
	}
}
