package cn.lucifer.demo.syntax;

import org.junit.Test;

public class Question {

	/**
	 * 计算年龄
	 */
	@Test
	public void testToOld() {
		final int[] arr = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		for (int i = 0; i < arr.length; i++) {
			int sum = arr[i] * 2;
			sum += 5;
			sum *= 50;
			System.out.println(sum);
			sum += 1763;// 基础修正值
			System.out.println(sum);
			System.out.println();
		}
	}

}
