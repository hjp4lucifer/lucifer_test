/**
 * 
 */
package cn.lucifer.demo.syntax;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class MenthodTest {
	private ObjectId s;
	private Long l;

	protected long myLong;

	@Test
	public void testMain() {
		to(s);
		if (l != 0) {
			System.out.println("no 0");
		}
	}

	@Test
	public void testGetter() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		MenthodTest t = new MenthodTest();
		t.myLong = 124;

		Method method = t.getClass().getMethod("getMyLong");
		System.out.println(method.invoke(t));
	}

	public void to(ObjectId id) {
		System.out.println("a!");
	}

	public void to(String id) {
		System.out.println("b!");
	}

	/**
	 * @return the {@link #myLong}
	 */
	public long getMyLong() {
		return myLong;
	}
}
