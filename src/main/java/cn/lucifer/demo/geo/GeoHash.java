package cn.lucifer.demo.geo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * 模拟mongodb的geo hash
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
				_hash |= mask64For((i * 2) + 1);
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
		log.info("r_hash=\t" + Long.toBinaryString(_hash) + " , hex=" + Long.toHexString(_hash));
	}
}
