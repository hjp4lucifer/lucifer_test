package cn.lucifer.demo.syntax;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.zip.CRC32;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import redis.clients.util.MurmurHash;

public class HashTest {

	@Test
	public void testHashSet() {
		long[] array = { 1, 2, 3, 4, 5 };
		HashSet<Long> hash = new HashSet<Long>();
		int index = 0;
		System.out.println(hash.add(array[index]));
		index++;
		System.out.println(hash.add(array[index]));
		System.out.println(hash.add(array[index]));

		System.out.println(hash);
	}

	@Test
	public void testCRC32() {
		char z = 'z';
		String str;
		HashMap<Integer, Integer> map = new HashMap<>();
		Integer key;
		for (int i = 0; i < 100000; i++) {
			char[] charArray = getRandomStr(z);
			str = new String(charArray);
			key = newCompatHashingAlg(str);
			// System.out.println(str + "\t" + key);

			// hash
			hashStat(map, key.intValue());
		}
		System.out.println(map);
	}

	@Test
	public void testCRC32File1() throws IOException {
		File file = new File("C:/test/222/pai2_user.csv");
		List<String> lines = FileUtils.readLines(file);
		HashMap<Integer, Integer> map = new HashMap<>();
		Integer key;
		String[] array;
		String str;
		for (String line : lines) {
			array = StringUtils.split(line, ',');
			str = array[2];
			str = StringUtils.trimToNull(StringUtils.remove(str, '\"'));
			if (null == str || str.length() < 5) {
				continue;
			}
			System.out.println(str);
			key = newCompatHashingAlg(str);
			// hash
			hashStat(map, key.intValue());
		}
		System.out.println(map);
	}
	
	@Test
	public void testCRC32File2() throws IOException {
		File file = new File("C:/test/222/common_user_dbshare");
		List<String> lines = FileUtils.readLines(file);
		HashMap<Integer, Integer> map = new HashMap<>();
		Integer key;
		String str;
		for (String line : lines) {
			str = StringUtils.trimToNull(RSAEncrypt.decryptParam(line));
			if (null == str || str.length() < 5) {
				continue;
			}
			//System.out.println(str);
			key = newCompatHashingAlg(str);
			// hash
			hashStat(map, key.intValue());
		}
		System.out.println(map);
	}

	@Test
	public void testMurmurHash() {
		char z = 'z';
		String str;
		HashMap<Integer, Integer> map = new HashMap<>();
		Long key;
		MurmurHash hash = new MurmurHash();
		for (int i = 0; i < 1000000; i++) {
			char[] charArray = getRandomStr(z);
			str = new String(charArray);
			// key = newCompatHashingAlg(str);
			key = hash.hash(str.getBytes());
			// System.out.println(str + "\t" + key);

			// hash
			hashStat(map, key.intValue());
		}
		System.out.println(map);
	}

	@Test
	public void testSumStr() {
		char z = 'z';
		String str;

		HashMap<Integer, Integer> map = new HashMap<>();
		Long key;
		MurmurHash hash = new MurmurHash();
		for (int i = 0; i < 1000000; i++) {
			char[] charArray = getRandomStr(z);
			str = new String(charArray);
			key = sumStr(charArray);
			// System.out.println(str + "\t" + key);

			// hash
			hashStat(map, key.intValue());
		}
		System.out.println(map);

	}

	protected long sumStr(char[] charArray) {
		long sum = 0;
		for (int i = 0; i < charArray.length; i++) {
			sum += charArray[i] + i;
		}
		return sum;
	}

	protected char[] getRandomStr(char z) {
		int len = (int) (Math.random() * 5) + 30;
		char[] charArray = new char[len];
		for (int j = 0; j < len; j++) {
			charArray[j] = (char) (z - (int) (Math.random() * 25));
		}
		return charArray;
	}

	protected void hashStat(HashMap<Integer, Integer> map, Integer key) {
		Integer val;
//		key = Math.abs(key % 10);
		key = key & 0x1F;
		val = map.get(key);
		if (null == val) {
			val = 0;
		}
		val++;
		map.put(key, val);
	}

	protected int newCompatHashingAlg(String key) {
		CRC32 checksum = new CRC32();
		checksum.update(key.getBytes());
		int crc = (int) checksum.getValue();

		return (crc >> 16) & 0x7fff;
	}
}
