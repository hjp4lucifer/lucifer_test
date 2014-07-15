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
		System.out.println( ~0);
	}
}
