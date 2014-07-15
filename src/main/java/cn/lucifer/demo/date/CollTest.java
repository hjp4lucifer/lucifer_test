/**
 * 
 */
package cn.lucifer.demo.date;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import cn.lucifer.util.DateUtils;

/**
 * @author Lucifer
 * 
 */
public class CollTest {
	@Test
	public void mainTest(String[] args) {
		Date date = DateUtils.getTime("1987-07-14 00:00:00");
		System.out.println(getAstro(date));
	}

	public String getAstro(int month, int day) {
		String s = "魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
		int[] arr = new int[] { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
		int startIndex = month * 2 - (day < arr[month - 1] ? 2 : 0);
		return s.substring(startIndex, startIndex + 2);
	}

	public String getAstro(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		System.out.println(c.get(Calendar.MONTH) + "--------");
		return getAstro(c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
	}
}
