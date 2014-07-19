package cn.lucifer.demo.file;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class IntuitiveStringComparatorTest {

	@Test
	public void test() {
		String[] list = { "foo 03", "foo 00003", "foo.txt", "foo 5", "foo 003",
				"foo~03", "foo03.txt", "foo 10far", "foo 10boo", "1.01.txt",
				"1.txt", "1.1.txt", "1.001.txt", "1.0.txt", "foo12.txt",
				"foo1.txt", "foo10.txt", "foo!03" };

		Arrays.sort(list, new IntuitiveStringComparator());
		for (String s : list) {
			System.out.println(s);
		}
	}

}
