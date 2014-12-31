/**
 * 
 */
package cn.lucifer.demo.string;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class RegexTest {

	@Test
	public void test1() {
		String regex = "\uD83D";
		String source = "\uD83D\uDC7F";
		showMathcer(regex, source);
	}

	@Test
	public void test2() {
		String s = "<font>为</font><font color=\"#FFFAF5\">知<font size=\"3\" face=\"Times New Roman\">&nbsp;</font></font>";
		String regex = "<font color=\"#FFFAF5\">.*?<font .*?>.*?</font></font>";
		showMathcer(regex, s);
	}

	public void showMathcer(String regex, String source) {
		Pattern expression = Pattern.compile(regex);
		Matcher matcher = expression.matcher(source);
		// System.out.println(matcher.find());
		while (matcher.find()) {
			System.out.println(matcher.group());
		}
	}
	
	@Test
	public void test3() {
		String regex = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		String source = "hjp_0002@163.com,hjp_0002@hotmail.com,dafdf@aa.com,dfae";

		Pattern expression = Pattern.compile(regex);
		Matcher matcher = expression.matcher(source);
		// System.out.println(matcher.find());
		while (matcher.find()) {
			System.out.println(matcher.group());
		}
	}

	@Test
	public void testSimple() {
		String regex = ";|(--)|'|\"";
		String[] sources = new String[] {
				"select sum(view_count),sum(income) from ad_user_report a, ad_product b  where a.coo_id='e018dc4da82d497ca6cd46ee083f78d1' AND a.coo_id = b.coo_id AND b.status_screen=3  and timeframe>='2014-05-01' or 1='1 00' and timeframe<='2014-05-08 14'",
				"aaa,dd", "aaaaaa;aa", "bbbbbb-bbbb", "cccccccc--ccc",
				"ddddd'dddd", "eeee\"eeeeee" };

		Pattern expression = Pattern.compile(regex);
		Matcher matcher;
		for (String s : sources) {
			matcher = expression.matcher(s);
			System.out.println(s);
			System.out.println(matcher.find());
		}
	}

	@Test
	public void test4() {
		// String id = "4d85afe1c8c84edcd707adff";
		// System.out.println(id.length());
		// String source =
		// "我的email是hjp@163.com@wind @a@好不 aja;k@fl,a, jkas@;fjwe @东西 dfd";
		String source = "我是http://www.mzpai.com 的推广啊https://mail.163.com";
		// String source = "aaa";
		// String regex = "@[^(\\s|;|,|。|，|；|、)]+(\\s|;|,|。|，|；|、)?";
		String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		String regex_mail = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		// String[] ss = source.split(regex);
		// System.out.println(ss.length);
		// for (String s : ss) {
		// System.out.println(s);
		// }
		source = source.replaceAll(regex_mail, "");
		Pattern expression = Pattern.compile(regex);
		Matcher matcher = expression.matcher(source);
		while (matcher.find()) {
			System.out.println(matcher.group());
		}

		// System.out.println("==================");
		// MatchResult matchResult =
		// RegexpUtils.getHardRegexpMatchResult(source, regex);
		// System.out.println(matchResult);
		// for (int i = 0; i < matchResult.groups(); i++) {
		// System.out.println(matchResult.group(i));
		// }
		System.out.println("ok!");
	}

	@Test
	public void testRemove() throws IOException {
		File file = new File("resource/string.txt");
		String txt = FileUtils.readFileToString(file);
		System.out.println(txt);
		System.out.println("\n\n\n\n");
		String regex = "<font .*?>(.*?)</font>";
		List<String> chilren = getMatchChildren( txt,regex);
		for (String str : chilren) {
			str = StringUtils.remove(str, '-');
			System.out.println(str);
		}
	}
	
	public List<String> getMatchChildren(String source, String regex) {
		Pattern expression = Pattern.compile(regex);
		Matcher matcher = expression.matcher(source);
		int groupCount;
		List<String> children = new ArrayList<String>();
		while (matcher.find()) {
			groupCount = matcher.groupCount();
			for (int i = 1; i <= groupCount; i++) {
				children.add(matcher.group(i));
			}
		}
		return children;
	}

	@Test
	public void testUserAgentFind() {
		String userAgent = "Mozilla/5.0 (iPhoe; U; CPU iPone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16";

		final String regex = "iphone|ipod|ipad|android"; 
		userAgent = userAgent.toLowerCase();
		System.out.println(Pattern.compile(regex).matcher(userAgent).find());
		System.out.println(userAgent.replaceAll(regex, ""));
	}
	
	@Test
	public void testMatch(){
		final String callback = "d";
		System.out.println(Pattern.matches("\\w+", callback));
	}
}
