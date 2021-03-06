/**
 * 
 */
package cn.lucifer.demo.string;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import cn.lucifer.util.DigestUtils;

/**
 * @author Lucifer
 * 
 */
public class StringTest {

	@Test
	public void testMain() {
		String name = "广东省";
		// System.out.println(fixName(name, '省'));
		// System.out.println(createAuthorizedCodeString(null, new Date()));
		System.out.println(StringUtils.removeEnd(name, "省"));
	}

	public String createAuthorizedCodeString(String password, Date lastLogin) {
		return password + lastLogin.getTime();
	}

	protected String fixName(String name, char fix) {
		if (name != null) {
			if (fix == name.charAt(name.length() - 1)) {
				name = name.substring(0, name.length() - 1);
			}
		}
		return name;
	}

	public void printString(String string) {
		System.out.println(string + System.currentTimeMillis());
	}
	
	@Test
	public void test(){
		byte[] rsp = null;
		System.out.println(rsp == null ? null : new String(rsp));
	}

	@Test
	public void testSql() {
		String sql = "insert user(id, name) values (124, 'Lucifer')";
		if (sql.toLowerCase().contains("insert"))
			sql = sql.substring(0, sql.toLowerCase().indexOf("("));
		if (sql.toLowerCase().contains("update"))
			sql = sql.substring(0, sql.toLowerCase().indexOf("set") - 1);

		System.out.println(sql);
	}

	@Test
	public void testDoublePoint() {
		String path = "path=%2F..%2Fserver0%2Fjiujunwww%2FWEB-INF%2Fweb.xml";
		path = StringUtils.replace(path, "..", "");
		System.out.println(path);
	}

	@Test
	public void testTime() throws ParseException {
		long time = 1441854000000L;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println(format.format(new Date(time)));

		System.out.println(format.parse("2015-09-10 11:00:00.000").getTime());
	}

	@Test
	public void testFileSuffix() {
		String fn = "abcd.jpeg";
		String suffix = getSuffix(fn);
		System.out.println(suffix);

		fn = "abcd";
		suffix = getSuffix(fn);
		System.out.println(suffix);

		fn = "abcd.";
		suffix = getSuffix(fn);
		System.out.println(suffix);
	}

	protected String getSuffix(String fn) {
		String suffix = ".jpg";
		int lastIndex = fn.lastIndexOf('.');
		if (lastIndex > 0) {
			suffix = fn.substring(lastIndex);
		}
		fn = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + suffix;
		System.out.println(fn);
		return suffix;
	}

	@Test
	public void testLength() {
		String s = "aabb";
		s = "aabb啊";
		System.out.println(s.getBytes().length);
	}

	@Test
	public void testProcess() {
		String str = "com.iojia.app.ojiasns:1000000168766026";
		int lastIndex = str.lastIndexOf(":");
		str = str.substring(lastIndex + 1);
		System.out.println(str);
	}

	@Test
	public void testDateFormater() {
		SimpleDateFormat format = new SimpleDateFormat("hh-mm:ss");
		long time = 3663000L;
		System.out.println(format.format(time));
	}

	@Test
	public void testMd5() {
		String s = "cid=14830870&ts=1488767209";
		String sign = DigestUtils.md5DigestAsHex(s.getBytes());
		System.out.println(sign);
	}
}
