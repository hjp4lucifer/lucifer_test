/**
 * 
 */
package cn.lucifer.demo.string;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}
