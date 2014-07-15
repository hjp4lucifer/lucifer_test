package cn.lucifer.demo.string;

import org.junit.Test;

public class LongTest {

	Long l;

	@Test
	public void testMain() {
		String sid = "3331765905184136";
		Long lid = Long.parseLong(sid);
		System.out.println(lid);
		System.out.println(Long.toHexString(lid));
		System.out.println(l);
		System.out.println(2157262361L > 2147483647L);
	}
}
