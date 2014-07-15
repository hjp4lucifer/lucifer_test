package cn.lucifer.demo.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import cn.lucifer.demo.http.domain.ArrPic;
import cn.lucifer.demo.http.domain.PhotoData;
import cn.lucifer.demo.http.domain.PicJson;
import cn.lucifer.util.HttpClientHelper;

import com.google.gson.Gson;

public class QQDownloadDemo {

	private static final String path = "resource/down.json";

	/**
	 * 腾讯高清新闻相册可用
	 * 
	 * @param args
	 * @throws IOException
	 */
	@Test
	public void testMain(String[] args) throws IOException {
		FileReader reader = new FileReader(new File(path));

		// String baseSavePath = "D:/pic/cos/showgirl/张力文/chianjoy2012";
		// String baseSavePath = "D:/pic/cos/ChinaJoy/2012/无法忘怀";
		// String baseSavePath = "D:/pic/cos/EVA/明日香";
		// String baseSavePath = "D:/pic/model/柳岩/3月8日《天天向上》";
		// String baseSavePath = "D:/pic/model/张馨予/穿透视装拍写真";
		// String baseSavePath = "D:/pic/star/China/F/范冰冰/2013奥斯卡";
		// String baseSavePath = "D:/pic/model/曹阳/第13届武汉国际车展";
		// String baseSavePath = "D:/pic/model/艾尚真/COS战场女王";
		// String baseSavePath = "D:/pic/model/湿露露/重庆第二届车博会";
		// String baseSavePath = "D:/pic/2012广州车展/雪铁龙C4L";
		// String baseSavePath = "D:/pic/model/周韦彤/“中国星跳跃”宣传照";
		// String baseSavePath = "D:/pic/model/王笛/真空上阵";
		String baseSavePath = "D:/pic/model/吴沐熙/恒大球衣";
		File f = new File(baseSavePath);
		if (!f.exists()) {
			f.mkdirs();
		}

		PhotoData data = new Gson().fromJson(reader, PhotoData.class);
		PhotoData root = data.Children.get(0);
		for (PhotoData d : root.Children) {
			if ("groupimg".equalsIgnoreCase(d.Name)) {
				for (PhotoData pd : d.Children) {
					String url = null;
					for (PhotoData pd3 : pd.Children) {
						if ("bigimgurl".equalsIgnoreCase(pd3.Name)) {
							PhotoData pd4 = pd3.Children.get(0);
							url = pd4.Content;
						}
						if ("source".equalsIgnoreCase(pd3.Name)) {
							PhotoData pd4 = pd3.Children.get(0);
							if (StringUtils.isBlank(pd4.Content)) {
								url = pd4.Content;
								break;
							}
						}
					}
					url = StringUtils.trimToNull(url);
					if (url != null) {
						saveFile(url, baseSavePath);
					}
				}

				break;// 其他的可以忽略
			}
		}
	}

	private final String source_path = "resource/down2.json";

	@Test
	public void main4mshow() throws IOException {
		final String baseURL = "http://img1.gtimg.com/mshow_imgs";
		Gson gson = new Gson();
		FileReader reader = new FileReader(new File(source_path));
		String baseSavePath = "D:/pic/2012广州车展/英菲尼迪/4";
		File f = new File(baseSavePath);
		if (!f.exists()) {
			f.mkdirs();
		}

		PicJson data = gson.fromJson(reader, PicJson.class);
		String url;
		int zoom = data.getoZoomConfig()[data.getoZoomConfig().length - 1];
		String zoomString = zoom + ".";
		for (ArrPic pic : data.getArrPic()) {
			url = baseURL + pic.getsImg().replace(".", zoomString);
			saveFile(url, baseSavePath);
		}
	}

	private final String source = "[[107,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/a33485ffc26ded9f1d05cfa7666262a93.jpg'],[101,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/c851dc2c9623313dd40d18e96db4feb53.jpg'],[102,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/02128a698065e0b71cd9da4209c3aa353.jpg'],[103,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/aa18def1a65110ad8870da6c0bdd5ec33.jpg'],[104,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/2907e9b8a94a58207ee0b72ae3a945ad3.jpg'],[105,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/ecb0e3ae652ce35f35e33d8a9439a39f3.jpg'],[106,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/37cdb81c01f57a42172793cd5dcf6efc3.jpg'],[92,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/428b23fe4b222ea3dbddeff455931cec3.jpg'],[91,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/4441c5c5f9a158ccb9c05a721113b6233.jpg'],[93,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/665d27c6130f197f3095fe1d52e154273.jpg'],[94,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/a4a12820b954b3ddda8cebe59dc4642f3.jpg'],[95,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/854640cdc4019762490e96e8635231833.jpg'],[96,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/20e1337fe865e6bca611dd93c43ff2c33.jpg'],[97,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/205e3215c1d110ef3fbe46021b06d44d3.jpg'],[98,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/1f7ed199896fa4b0cd460034a901f7a23.jpg'],[99,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/897500394cb34ec9d4f8bd1d8952e32e3.jpg'],[100,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/6def8bbabf7cba4be03a3a7b432963933.jpg'],[85,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/1dd8257b8623230349f0fca4851464193.jpg'],[86,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/e0fd0c5e0a911bce3604bee4e877327b3.jpg'],[87,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/5a24b29465dd72bc53c41a9edf9e600f3.jpg'],[88,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/ce0302d160cf7d103f5d5dece0ce17933.jpg'],[89,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/9d7884332b14944c1440c806963a1a043.jpg'],[90,'冯莺如','http://img2.gtimg.com/2011/piclib/20110717/c78631f5ac4d3da6795b4476d5d9666b3.jpg']]";

	private final String http_regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

	/**
	 * qq图片
	 * 
	 * @param args
	 * @throws IOException
	 */
	@Test
	public void main4showGirl() throws IOException {
		List<String> urls = showMathcer(http_regex, source);

		String baseSavePath = "D:/pic/showgirl/冯莺如";
		File f = new File(baseSavePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		for (String url : urls) {
			url = url.replace("3.", ".");
			saveFile(url, baseSavePath);
		}

		// String url = null;
		// byte[] data = HttpClientHelper.httpGet(url, null);
		// System.out.println(new String(data, "gbk"));
	}

	public void saveFile(String url, String baseSavePath) throws IOException {
		byte[] data = HttpClientHelper.httpGet(url, null);
		String pathname = baseSavePath + url.substring(url.lastIndexOf("/"));
		OutputStream output = new FileOutputStream(new File(pathname));
		IOUtils.write(data, output);
		IOUtils.closeQuietly(output);
		System.out.println("save finish : " + pathname);
	}

	public static List<String> showMathcer(String regex, String source) {
		Pattern expression = Pattern.compile(regex);
		Matcher matcher = expression.matcher(source);
		// System.out.println(matcher.find());
		List<String> result = new ArrayList<String>();
		String matcherString;
		while (matcher.find()) {
			matcherString = matcher.group();
			result.add(matcherString);
			System.out.println(matcherString);
		}
		System.out.println("======================================");
		return result;
	}
}
