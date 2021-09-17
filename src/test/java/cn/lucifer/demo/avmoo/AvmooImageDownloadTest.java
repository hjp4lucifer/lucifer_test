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

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void download() throws Exception {

//		AvmooStar star = new AvmooStar("桃乃木かな", "https://avmoo.casa/cn/star/6a85f721d4b46ea4", true);
//		AvmooStar star = new AvmooStar("三上悠亜", "https://avmoo.casa/cn/star/aa1709fc6ffeb4f6", true);
//		AvmooStar star = new AvmooStar("七ツ森りり", "https://avmoo.casa/cn/star/17f01576bb6b6755", true);
		AvmooStar star = new AvmooStar("篠田ゆう", "https://avmoo.casa/cn/star/44ea747cc9811733");

		final File savePath = new File("D:\\limit3\\" + star.getName());

		AvmooImageDownload avmooImageDownload = new AvmooImageDownload(star, savePath);
		avmooImageDownload.downloadAndSave();

	}


}