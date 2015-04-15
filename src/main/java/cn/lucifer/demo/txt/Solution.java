package cn.lucifer.demo.txt;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Solution {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	public int[] plusOne(int[] digits) {
		if (null == digits || digits.length == 0) {
			return new int[] { 1 };
		}
		int d;
		boolean up = true;
		final int len = digits.length;
		for (int i = len - 1; i >= 0; i--) {
			d = digits[i];
			if (9 != d) {
				digits[i] = d + 1;
				up = false;
				break;
			}
			digits[i] = 0;
		}
		if (up) {
			int[] rDigits = new int[len + 1];
			rDigits[0] = 1;
			System.arraycopy(digits, 0, rDigits, 1, len);
			return rDigits;
		} else {
			return digits;
		}
	}

	@Test
	public void test() {
		int len = 100000000;
		final int[] digits = new int[len];
		for (int i = 0; i < len; i++) {
			digits[i] = 9;
		}

		final long start = System.currentTimeMillis();
		int[] results = new Solution().plusOne(digits);
		System.out.println(System.currentTimeMillis() - start);
		
		for (int i : results) {
			//System.out.print(i);
		}
	}

}
