package cn.lucifer.demo.syntax;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class NumberTest {

	@Test
	public void testMain() {
		// int i = 9 / 10;
		// System.out.println(i);
		// i = 1024;
		// i = i >> 10;
		// System.out.println(i);
		int a = Integer.parseInt("11001", 2), b = 0x19;
		System.out.println(a & (1 << 2));
	}

	@Test
	public void testToSting() {
		String s = Integer.toBinaryString(1);
		System.out.println(s);
		s = String.format("%04d", 10020);
		s = String.format("%04d", 20);
		s = s.substring(s.length() - 4);
		System.out.println(s);
	}

	@Test
	public void testRandom() {
		for (int i = 0; i < 20; i++) {
			int r = (int) (Math.random() * 1000000);
			// System.out.println(r);
			System.out.println(String.format("%06d", r));
		}
	}

	@Test
	public void testIsNumber() {
		String str = "1101";
		System.out.println(StringUtils.isNumeric(str));
	}

	@Test
	public void testLong2Str() {
		long l = 13763305524L;
		System.out.println(Long.toHexString(l));
	}

	@Test
	public void testRandomInt() {
		for (int i = 0; i < 10; i++) {
			String str = Integer.toString(
					i * 100 + (int) (Math.random() * 100), 36);
			System.out.println(str);
		}
	}

	@Test
	public void testRandomLong() {
		String str = Long.toString(System.currentTimeMillis() * 100
				+ (int) (Math.random() * 100), 36);
		System.out.println(str);
	}

	/**
	 * <ul>
	 * <li>算法说明：当n大于2时，n个数的全组合一共有(2^n)-1种。</li>
	 * <li>当对n个元素进行全组合的时候，可以用一个n位的二进制数表示取法。</li>
	 * <li>1表示在该位取，0表示不取。例如，对ABC三个元素进行全组合， 100表示取A，010表示取B，001表示取C，</li>
	 * <li>101表示取AC 110表示取AB，011表示取BC，111表示取ABC</li>
	 * <li>注意到表示取法的二进制数其实就是从1到7的十进制数</li>
	 * <li>推广到对n个元素进行全排列，取法就是从1到2^n-1的所有二进制形式</li>
	 * <li>要取得2^n，只需将0xFFFFFFFF左移32-n位，再右移回来就可以了。</li>
	 * <li></li>
	 * </ul>
	 */
	@Test
	public void testZuheArray() {
		String str[] = { "胸", "手", "腹", "小腿", "大腿", "臀部" };
		int arrayLen = str.length;
		int nBit = (0xFFFFFFFF >>> (32 - arrayLen));
		for (int i = 1; i <= nBit; i++) {
			System.out.print(i + "\t");
			for (int j = 0; j < arrayLen; j++) {
				if ((i << (31 - j)) >> 31 == -1) {
					System.out.print(str[j] + "\t");
				}
			}
			System.out.println();
		}
	}

	@Test
	public void testZuhe() {
		int max = 6;
		int sum = 0;
		int count;
		for (int i = 1; i <= max; i++) {
			count = zuhe(i, max);
			System.out.println(count);
			sum += count;
		}
		System.out.println(sum);
	}

	public int zuhe(int n, int m) {
		return jiecheng(m) / (jiecheng(n) * jiecheng(m - n));
	}

	public int jiecheng(int num) {
		int sum = 1;
		for (int i = num; i > 1; i--) {
			sum *= i;
		}
		return sum;
	}

}
