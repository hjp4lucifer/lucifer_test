/**
 * 
 */
package cn.lucifer.demo.syntax;

import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class MenthodTest {
	private ObjectId s;
	private Long l;

	@Test
	public void testMain() {
		to(s);
		if (l != 0) {
			System.out.println("no 0");
		}
	}

	public void to(ObjectId id) {
		System.out.println("a!");
	}

	public void to(String id) {
		System.out.println("b!");
	}
}
