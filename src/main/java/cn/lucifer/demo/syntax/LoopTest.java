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
}
