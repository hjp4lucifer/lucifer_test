package cn.lucifer.demo.syntax;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class NumberTest {

	@Test
	public void testMain() {
		// int i = 9 / 10;
		// System.out.println(i);
		// i = 1024;
		// i = i >> 10;
		// System.out.println(i);
		int a = Integer.parseInt("11001", 2), b = 0x19;
		System.out.println(a & (1 << 2));
	}

	@Test
	public void testToSting() {
		String s = Integer.toBinaryString(1);
		System.out.println(s);
		s = String.format("%04d", 10020);
		s = String.format("%04d", 20);
		s = s.substring(s.length() - 4);
		System.out.println(s);
	}

	@Test
	public void testRandom() {
		for (int i = 0; i < 20; i++) {
			int r = (int) (Math.random() * 1000000);
			// System.out.println(r);
			System.out.println(String.format("%06d", r));
		}
	}

	@Test
	public void testIsNumber() {
		String str = "1101";
		System.out.println(StringUtils.isNumeric(str));
	}

	@Test
	public void testLong2Str() {
		long l = 13763305524L;
		System.out.println(Long.toHexString(l));
	}

	@Test
	public void testRandomInt() {
		for (int i = 0; i < 10; i++) {
			String str = Integer.toString(
					i * 100 + (int) (Math.random() * 100), 36);
			System.out.println(str);
		}
	}

	@Test
	public void testRandomLong() {
		String str = Long.toString(System.currentTimeMillis() * 100
				+ (int) (Math.random() * 100), 36);
		System.out.println(str);
	}
}
