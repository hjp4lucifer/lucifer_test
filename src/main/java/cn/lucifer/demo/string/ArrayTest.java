/**
 * 
 */
package cn.lucifer.demo.string;

import java.util.Collection;

import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class ArrayTest {

	@Test
	public void testMain() {
		getSimpleByIds(null, true, "a");
	}

	public void getSimpleByIds(Collection<String> photoIds,
			boolean includePrivate, String... fields) {
		System.out.println(fields.length);
		System.out.println(~0);
	}

	@Test
	public void testRunArray() {
		System.out.println("start:\t" + Runtime.getRuntime().freeMemory());
		runArray();
		System.out.println("end:\t" + Runtime.getRuntime().freeMemory());
	}

	@Test
	public void testRunArray4Loop() {
		long startFreeMemory = Runtime.getRuntime().freeMemory();
		for (int i = 0; i < 10000; i++) {
			System.out.print(i + "-");
			testRunArray();
		}
		long endFreeMemory = Runtime.getRuntime().freeMemory();
		System.out.println(String.format(
				"result: startFreeMemory=%d, endFreeMemory=%d",
				startFreeMemory, endFreeMemory));
	}

	private void runArray() {
		int[][] array = new int[256][256];
		System.out.println(array[1][3]);
	}
}
