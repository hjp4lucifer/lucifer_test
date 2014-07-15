package cn.lucifer.demo.syntax;

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
		System.out.println(a &  (1 << 2));
	}

	@Test
	public void testToSting(){
		String s = Integer.toBinaryString(1);
		System.out.println(s);
		s = String.format("%04d", 10020);
		s = String.format("%04d", 20);
		s = s.substring(s.length() - 4);
		System.out.println(s);
	}
}
