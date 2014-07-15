package cn.lucifer.demo.jedis;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class SimpleTest {
	private Jedis j;

	@Before
	public void setUp() throws Exception {
		j = new Jedis("192.168.1.151", 6379);
		j.auth("foobared");
	}

	@Test
	public void testSetGetWithTimeout() {
		String key = "lucifer";
		String value = j.get(key);
		System.out.println(key + " : " + value);

		if (value == null) {
			String status = j.set(key, "hi " + key);
			System.out.println("status : " + status);
		} else {
			Long delCount = j.del(key);
			System.out.println("delect count : " + delCount);
		}
		try {
			Thread.sleep(10 * 30000);
		} catch (InterruptedException e) {
		}
		System.out.println("is connected : " + j.isConnected());
		System.out.println("ping : " + j.ping());
		if (!j.isConnected()) {
			j.connect();
		}

		System.out.println("is connected : " + j.isConnected());
		value = j.get(key);
		System.out.println(key + " : " + value);

	}

	/**
	 * maxmemory为1024*1024=1048576 bytes=1M
	 * 
	 * @throws redis.clients.jedis.exceptions.JedisDataException
	 *             : OOM command not allowed when used memory > 'maxmemory'.
	 */
	@Test
	public void testRedisMemory() {
		String key = "lucifer";
		String formatKey = "%s_value_%09X";
		String formatValue = "%s_value_%09X";
		for (int i = 0; i < 10000000; i++) {
			System.out.println(i);
			j.set(String.format(formatKey, key, i),
					String.format(formatValue, key, i));
		}
	}

	public static void main(String[] args) {
		Jedis j;
		j = new Jedis("192.168.1.151", 6379, 0);
		j.auth("foobared");
		String key = "lucifer";
		String formatKey = "%09X";
		String formatValue = "%09X";
		int init = (int) (Math.random() * 10) * 10000000;
		int max = init + 10000000;
		long start = System.currentTimeMillis();
		for (int i = init; i < max; i++) {
			System.out.println(i);
			j.hset(key, String.format(formatKey, i),
					String.format(formatValue, i));
		}
		long end = System.currentTimeMillis();
		System.out.println("used time : " + (end - start));
	}
}
