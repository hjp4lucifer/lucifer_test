/**
 * 
 */
package cn.lucifer.demo.character;

/**
 * @author Lucifer
 * 
 */
public class CharTest {

	public static void main1(String[] args) {
		// char a = '０', z = '９';
		// for (int i = a; i <= z; i++) {
		// System.out.println((char) i);
		// }
		// char b = '1';
		// System.out.println(String.format("%H", b));

		String s = "";
		for (int i = 0; i < s.length(); i++) {
			System.out.println(String.format("%H", s.charAt(i)));
		}
		System.out.println(String.format("%H", '\uF604'));
		System.out.println('\uF604' == s.charAt(0));
	}
}
