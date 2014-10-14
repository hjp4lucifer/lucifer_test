package cn.lucifer.demo.syntax;

import java.util.HashSet;

import org.junit.Test;

public class HashTest {

	@Test
	public void testHashSet() {
		long[] array = { 1, 2, 3, 4, 5 };
		HashSet<Long> hash = new HashSet<Long>();
		int index = 0;
		System.out.println(hash.add(array[index]));
		index++;
		System.out.println(hash.add(array[index]));
		System.out.println(hash.add(array[index]));
		
		System.out.println(hash);
	}
}
