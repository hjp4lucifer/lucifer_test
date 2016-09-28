package cn.lucifer.demo.syntax;

import org.junit.Test;

public class LoopTest {
	@Test
	public void testMain() {
		Loop:

		for (int i = 2; i < 10; i++) {

			for (int j = 2; j < i; j++) {
				if (i % j == 0)
					continue Loop;
			}

			System.out.print(i + " ");
		}
	}

	@Test
	public void testArray() {
		String[] str = "12,4,63".split(",");
		System.out.println(str.length);
	}

	@Test
	public void testBreak() {
		int i = 0, j = 0;
		abc: for (i = 0; i < 5000; i++) {
			def: for (j = 10000; j < 150000; j++) {
				if (i == 3 && j == 10003) {
					break abc;
				}
			}
		}
		System.out.println(i + "," + j);
		
	}
}
