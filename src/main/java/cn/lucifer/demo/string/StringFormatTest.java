/**
 * 
 */
package cn.lucifer.demo.string;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Lucifer
 * 
 */
public class StringFormatTest {

	@Test
	public void testMain() {
		String name = null;
		boolean is = false;
		System.out.println(String.format("aa %s , %b", name, is));
	}

	@Test
	public void testDateFormat() throws ParseException {
		String[] strArray = { "2014-03-05 9", "2014-03-05 10", "2014-03-05 0",
				"2014-03-05 22", "2014-12-23 0" };
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H");
		Date time;
		for (String str : strArray) {
			time = format.parse(str);
			System.out.println(str + " : " + time + " : " + time.getTime());
		}
	}

	@Test
	public void testNumberFormat() {
		String result = String.format("%05X", 800000);
		System.out.println(result);
		result = String.format("%05X", 3);
		System.out.println(result);
	}

	@Test
	public void testPrint() {
		print(null);
	}

	protected void print(Long id) {
		String str = String.format("id=%d", id);
		System.out.println(str);
	}

	@Test
	public void testNumberShow() {
		int i = Integer.MAX_VALUE / 10;
		System.out.println(i);
		System.out.println(String.format("%.2f", i / 100f));
	}

	@Test
	public void testNumberFix() {
		int memory = 320;
		if (memory > 100) {
			int h, l;
			h = memory / 100;
			l = memory % 100;
			System.out.println(String.format("%d.%02d", h, l));
		} else {
			System.out.println(memory / 100d);
		}
	}

	@Test
	public void testNumberFix4Long() {
		long memory = Long.MAX_VALUE;
		memory = 12;
		if (memory > 100) {
			long h, l;
			h = memory / 100;
			l = memory % 100;
			System.out.println(String.format("%d.%02d", h, l));
		} else {
			System.out.println(memory / 100d);
		}
	}

	@Test
	public void testIp() {
		String ip = "127.0.0.1";
		System.out.println(ip.substring(0, ip.lastIndexOf(".")));
		ip = "1.";
		System.out.println(ip.substring(ip.lastIndexOf(".") + 1));
	}

	@Test
	public void testPrice() {
		String unitPrice = "12";
		// 转单位为元
		if (unitPrice.length() > 2) {
			unitPrice = new StringBuffer(unitPrice).insert(
					unitPrice.length() - 2, '.').toString();
		} else {
			try {
				int memory = Integer.parseInt(unitPrice);
				unitPrice = String.valueOf(memory / 100d);
			} catch (NumberFormatException e) {
			}
		}

		System.out.println(unitPrice);
	}

	@Test
	public void testJSON() {
		JSONObject jObject = JSON.parseObject("啊哈");
	}
}
