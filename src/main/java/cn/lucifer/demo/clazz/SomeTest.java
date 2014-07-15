/**
 * 
 */
package cn.lucifer.demo.clazz;

import org.junit.Test;

import com.google.gson.Gson;

/**
 * @author Lucifer
 * 
 */
public class SomeTest {

	/**
	 * @param args
	 */
	@Test
	public void testMain() {
		// trace(new B());
		// clone(new B());
		System.out.println(new B());
		// System.out.println(new AS().a);
	}

	public void trace(A a) {
		Class clazz = a.getClass();
		System.out.println(clazz);
	}

	public void clone(A a) {
		try {
			System.out.println(new Gson().toJson(a.clone()));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

}
