package cn.lucifer.demo.geo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * 模拟mongodb的geo hash
 * 
 * @author Lucifer
 *
 */
public class GeoHash {

	public static final Log log = LogFactory.getLog(GeoHash.class);

	long hash(int x, int y, int bits) {
		long _hash = 0;
		for (int i = 0; i < bits; i++) {
			if (isBitSet(x, i)) {
				_hash |= mask64For(i * 2);
				log.debug("_hash=\t" + Long.toBinaryString(_hash));
			}
			if (isBitSet(y, i)) {
				_hash |= mask64For((i * 2) + 1);// 用相邻位表示, 容易生成接近值
				log.debug("_hash=\t" + Long.toBinaryString(_hash));
			}
		}
		return _hash;
	}

	/** Is the 'bit'-th most significant bit set? (NOT the least significant) */
	boolean isBitSet(int val, int bit) {
		log.debug("val=\t" + Integer.toBinaryString(val));
		int i = mask32For(bit) & val;
		log.debug("isBitSet=\t" + Integer.toBinaryString(i));
		return i == 0 ? false : true;
	}

	// For i return the i-th most significant bit.
	// masks(0) = 80000..000
	// masks(1) = 40000..000
	// etc.
	// Number of 0s depends on 32 vs. 64 bit.
	int mask32For(final int i) {
		int mask = 1 << (31 - i);
		log.debug("mask32=\t" + Integer.toBinaryString(mask));
		return mask;
	}

	long mask64For(final int i) {
		long mask = 1L << (63 - i);
		log.debug("mask64=\t" + Long.toBinaryString(mask));
		return mask;
	}

	@Test
	public void testHash() {
		int[] xArray = { 120, 110, 38, 45, 46, 44 };
		int[] yArray = { 110, 120, 38, 45, 46, 44 };
		int indxe = 1;
		int x = xArray[indxe];
		int y = yArray[indxe];
		int bits = 26;
		long _hash = hash(x, y, bits);
		log.info("r_hash=\t" + Long.toBinaryString(_hash) + " , hex="
				+ Long.toHexString(_hash));
	}

	@Test
	public void testHashByNear() {
		int[] xArray = { 120, 110, 109, 111, 110, 110, 110, 111 };
		int[] yArray = { 110, 120, 119, 120, 121, 122, 119, 121 };
		long[] hashArray = new long[xArray.length];

		final int bits = 32;
		int textX = 0, textY = 0;

		for (int index = 0; index < hashArray.length; index++) {
			textX += xArray[index];
			textY += yArray[index];
			hashArray[index] = hash(xArray[index], yArray[index], bits);
			log.info("r_hash" + index + "=\t"
					+ Long.toBinaryString(hashArray[index]) + " , hex="
					+ Long.toHexString(hashArray[index]) + " , "
					+ hashArray[index]);
			textX = textX >> 1;
			textY = textY >> 1;
			if (index > 1) {
				log.warn(String.format("r_hash%d-r_hash1=\t%d",
						index, hashArray[index] - hashArray[1]));
			}
		}

		log.info(textX + ", " + textY);
		long _hash = hash(textX, textY, bits);
		log.info("r_hash=\t" + Long.toBinaryString(_hash) + " , hex="
				+ Long.toHexString(_hash) + " , " + _hash);

		log.info(String.format("des hash1=%d, hash2=%d",
				(hashArray[0] - _hash), (hashArray[1] - _hash)));
	}
}
