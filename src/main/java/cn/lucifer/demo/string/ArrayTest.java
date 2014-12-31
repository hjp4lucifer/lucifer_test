/**
 * 
 */
package cn.lucifer.demo.string;

import java.util.ArrayList;
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
			runArray();
			// System.out.print(i + "-");
			// testRunArray();
		}
		long endFreeMemory = Runtime.getRuntime().freeMemory();
		System.out.println(String.format(
				"result: startFreeMemory=%d, endFreeMemory=%d, diff=%d",
				startFreeMemory, endFreeMemory,
				(startFreeMemory - endFreeMemory)));
	}

	private final int[][] _array = { { 0, 1, 2 }, { 22, 33, 5 }, { 23, 34, 7 },
			{ 25, 37, 9 } };

	private void runArray() {
		// int[][] array = new int[256][256];
		int[][] array = { { 0, 1, 2 }, { 22, 33, 5 }, { 23, 34, 7 },
				{ 25, 37, 9 } };
		// String a = "My God !!!! What is happend!!!!!";
		// System.out.println(array[1][2]);
		// int[] array= new int[256];
		// System.out.println(array[1]);
	}

	@Test
	public void testArrayClass() {
		ArrayTest[] array = new ArrayTest[3];
		System.out.println(ArrayTest.class.getName());
		System.out.println(array.getClass().getName());
		System.out.println(array.getClass().getSuperclass().getName());
		ArrayList<ArrayTest> list = new ArrayList<ArrayTest>(3);
		System.out.println(list.getClass().getName());
		System.out.println(list.toArray().getClass().getName());
	}

	@Test
	public void testArrayListToArray(){
		ArrayList<Object> args = new ArrayList<Object>();
		args.add("hi");
		args.add("my");
		Object[] objs = args.toArray();
		for (Object obj : objs) {
			System.out.println(obj);
		}
	}
}
