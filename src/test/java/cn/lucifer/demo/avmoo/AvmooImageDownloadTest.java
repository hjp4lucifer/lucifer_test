package cn.lucifer.demo.avmoo;

import cn.lucifer.util.HttpClientHelper;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AvmooImageDownloadTest {

	private static final String baseUrl = "https://avmoo.cfd/cn/star/";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void download() throws Exception {

		AvmooStar star = new AvmooStar("桃乃木かな", baseUrl + "6a85f721d4b46ea4", true);
//		AvmooStar star = new AvmooStar("三上悠亜", baseUrl + "aa1709fc6ffeb4f6", true);
//		AvmooStar star = new AvmooStar("七ツ森りり", baseUrl + "17f01576bb6b6755", true);
//		AvmooStar star = new AvmooStar("篠田ゆう", baseUrl + "44ea747cc9811733", true);
//		AvmooStar star = new AvmooStar("伊藤舞雪", baseUrl + "88d230eff70baf29", true);
//		AvmooStar star = new AvmooStar("小島みなみ", baseUrl + "85ac395eaf2003e0", true);
//		AvmooStar star = new AvmooStar("桜空もも", baseUrl + "e6d9d8be49e6ea05", true);
//		AvmooStar star = new AvmooStar("瀬乃みなみ", baseUrl + "22c81b1a0de11ecf", true);

		final File savePath = new File("E:\\limit\\" + star.getName());

		AvmooImageDownload avmooImageDownload = new AvmooImageDownload(star, savePath);
		avmooImageDownload.downloadAndSave();

	}


}