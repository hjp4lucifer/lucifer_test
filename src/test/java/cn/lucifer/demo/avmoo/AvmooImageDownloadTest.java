package cn.lucifer.demo.avmoo;

import cn.lucifer.util.HttpClientHelper;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.util.Map;

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
 		ImmutableList<AvmooStar> list = ImmutableList.<AvmooStar>builder()
				.add(new AvmooStar("桃乃木かな", baseUrl + "6a85f721d4b46ea4", true))
				.add(new AvmooStar("三上悠亜", baseUrl + "aa1709fc6ffeb4f6", true))
				.add(new AvmooStar("七ツ森りり", baseUrl + "17f01576bb6b6755", true))
				.add(new AvmooStar("篠田ゆう", baseUrl + "44ea747cc9811733", true))
				.add(new AvmooStar("伊藤舞雪", baseUrl + "88d230eff70baf29", true))
				.add(new AvmooStar("小島みなみ", baseUrl + "85ac395eaf2003e0", true))
//				.add(new AvmooStar("桜空もも", baseUrl + "e6d9d8be49e6ea05", true))
				.add(new AvmooStar("瀬乃みなみ", baseUrl + "22c81b1a0de11ecf", true))
				.add(new AvmooStar("明里つむぎ", baseUrl + "e4b7ae7e8b52c8ca", true))
				.add(new AvmooStar("黒川すみれ", baseUrl + "bac14b95734416dc", true))
				.add(new AvmooStar("坂井千晴", baseUrl + "5c29980ed4e78fea", true))
//				.add(new AvmooStar("玉城夏帆", baseUrl + "836614b4c600cbc0", true))
				.add(new AvmooStar("西宮ゆめ", baseUrl + "51019f9c2a2d8a88", true))
				.add(new AvmooStar("月乃ルナ", baseUrl + "4c5897d337e8f80d", true))
				.build();

		for (AvmooStar star : list) {
			if (!"月乃ルナ".equals(star.getName())) {
				continue;
			}

			final File savePath = new File("E:\\limit\\" + star.getName());

			AvmooImageDownload avmooImageDownload = new AvmooImageDownload(star, savePath);
			avmooImageDownload.downloadAndSave();
		}

	}


}