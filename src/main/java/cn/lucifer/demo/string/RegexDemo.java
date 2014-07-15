/**
 * 
 */
package cn.lucifer.demo.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class RegexDemo {

	@Test
	public void testMain() {
		saveImageToLocal(
				"<div id=\"miniNav\" onClick=\"register0410('mininav',1);\"><div><a href=\"http://service.qq.com/\">腾讯客服</a><span>·</span><a href=\"http://www.qq.com/map/\">导航</a><span>·</span><a href=\"http://pay.qq.com/\">充值</a><span>·</span><a href=\"javascript:void(0)\" onClick=\"SetHome(this,window.location)\" target=\"_top\">设为首页</a><span>·</span><a href=\"http://3gqq.qq.com/\">手机腾讯网</a><span>·</span><a href=\"http://mail.qq.com\" class=\"qq\">QQ邮箱<img src=\"http://img1.qq.com/news/pics/9499/9499745.gif\" /></a></div><img src=\"http://img1.qq.com/news/pics/9499/tdd.gif\" />",
				"<img[^>]+>", "c:/");
	}

	/**
	 * source 文章内容 ，一般为一大段的html内容 regex <img[^>]+> ，匹配所有img标记的html内容 uploadPath
	 * 本地保存路径
	 * 
	 * @param source
	 * @param regex
	 * @param uploadPath
	 * @return
	 */
	public String saveImageToLocal(String source, String regex,
			String uploadPath) {
		Pattern expression = Pattern.compile(regex);
		Matcher matcher = expression.matcher(source);
		String word = null, extensions = null, result = null;
		int start;
		int end;
		while (matcher.find()) {
			word = matcher.group();
			start = word.indexOf("http://"); // 是否为http协议
			if (start != -1) {
				word = word.substring(start);
				System.out.println(word);
				// if (!word.startsWith("http://www.9151.cc")) { // 判断是否为本地域名
				// end = word.indexOf("\"");
				// word = word.substring(0, end);
				// extensions = word.substring(word.lastIndexOf("."));
				// result = saveImage(word, extensions, uploadPath);
				// }
			}

		}
		return source;
	}
}
