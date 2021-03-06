/**
 * 
 */
package cn.lucifer.demo.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class TimeDemo {

	DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	@Test
	public void testMain() throws ParseException {
		String timeString = "2011-07-02 15:06:54.772";
		Date date = timeFormat.parse(timeString);
		System.out.println(date.getTime());

		Calendar.getInstance().getTimeInMillis();
	}

	@Test
	public void test2() throws ParseException {
		Date date = new SimpleDateFormat("yyyyMMdd").parse("20140115");
		System.out.println(timeFormat.format(date));
		System.out.println(date.getTime());
	}

	@Test
	public void test3() {
		Date time = new Date(1390525684831L);
		System.out.println(timeFormat.format(time));
	}

	@Test
	public void testGLN() {
		// TODO Auto-generated method stub
		String time1 = "2011-07-11 23:59:59";
		String result = null;
		try {
			result = formatTime(time1, result);
			System.out.println(result);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = "Tue Jan 21 11:18:17 格林尼治标准时间+0800 2014";
		System.out.println(Calendar.getInstance(Locale.CHINESE).getTime());
		long time2 = System.currentTimeMillis();
		System.out.println(time2);
		System.out.println(String.valueOf(time2).length());
	}

	private String formatTime(String time1, String result)
			throws ParseException {
		String resultModelLabel = "%s %d:%d";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		long nowTime = calendar.getTimeInMillis();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long todayZeroTime = calendar.getTimeInMillis();
		Date askDay = format.parse(time1);
		long askTime = askDay.getTime();
		if (askTime < todayZeroTime) {
			// 不是今天
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			Calendar askCal = Calendar.getInstance();
			askCal.setTime(askDay);
			if (askCal.before(calendar)) {
				result = format.format(askCal.getTime());
			} else {
				calendar.setTime(today);
				int td = Math.abs(calendar.get(Calendar.DAY_OF_MONTH)
						- askCal.get(Calendar.DAY_OF_MONTH));
				if (td == 1) {
					result = String.format(resultModelLabel, "昨天",
							askCal.get(Calendar.HOUR_OF_DAY),
							askCal.get(Calendar.MINUTE));
				} else if (td == 2)
					result = String.format(resultModelLabel, "前天",
							askCal.get(Calendar.HOUR_OF_DAY),
							askCal.get(Calendar.MINUTE));
				else
					result = format.format(askDay.getTime());
			}
		} else {
			// 今天
			long tn = (nowTime - askTime) / 1000;
			if (tn < 60) {
				result = "刚刚";
			} else if (tn <= 3600) {
				result = tn / 60 + "分钟前";
			} else if (tn <= 86400) {
				result = tn / 3600 + "小时前";
			}
		}
		return result;
	}

	@Test
	public void testGetDownloadCount() throws Exception {
		System.out.println(getDay(timeFormat.parse("2014-07-24 11:06:54.772")));
	}

	protected int getDay(Date time) {
		long now = System.currentTimeMillis();
		if (null == time || now < time.getTime()) {
			return 0;
		}

		long day = (now - time.getTime()) / (3600000 * 24);
		System.out.println(day);
		return (int) day;
	}

	@Test
	public void testCalendar() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 24);
		System.out.println(timeFormat.format(c.getTime()));
	}

	@Test
	public void testGetDayIndex() {
		long dayEndTime = System.currentTimeMillis();
		long dayIndexCached = 0;
		long ONE_DAY_MILLISECONDS = 24 * 3600 * 1000;
		long ONE_HOUR_MILLISECONDS = 3600000;
		long now = System.currentTimeMillis();
		if (now >= dayEndTime) {
			Calendar c = Calendar.getInstance();
			if (c.get(Calendar.HOUR_OF_DAY)<4) {
				c.setTimeInMillis(now - 10 * ONE_HOUR_MILLISECONDS);
			}
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.set(Calendar.HOUR_OF_DAY, 4);
			dayEndTime = c.getTimeInMillis() + ONE_DAY_MILLISECONDS;
			dayIndexCached = dayEndTime / ONE_DAY_MILLISECONDS;
		}

		System.out.println(dayEndTime);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.format(new Date(dayEndTime)));
		System.out.println(dayIndexCached);
	}
}
