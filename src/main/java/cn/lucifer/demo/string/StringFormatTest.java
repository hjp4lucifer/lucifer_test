/**
 * 
 */
package cn.lucifer.demo.string;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

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
				"2014-03-05 22" };
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H");
		for (String str : strArray) {
			System.out.println(str + " : " + format.parse(str));
		}
	}

	@Test
	public void testNumberFormat() {
		String result = String.format("%02X", 1);
		System.out.println(result);
	}

}
