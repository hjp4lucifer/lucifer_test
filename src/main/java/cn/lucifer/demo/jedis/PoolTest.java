package cn.lucifer.demo.jedis;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class PoolTest {
	private String host = "192.168.1.151";
	private int port = 6379;

	public void testPool(){
		
	}
	
	@Test
	public void checkJedisIsReusedWhenReturned() {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), host, port);
		Jedis jedis = pool.getResource();
		jedis.auth("foobared");
		jedis.set("foo", "0");
		pool.returnResource(jedis);

		jedis = pool.getResource();
		jedis.auth("foobared");
		jedis.incr("foo");
		pool.returnResource(jedis);
		pool.destroy();
	}

	@Test
	public void testRedisMemory() {
		String key = "lucifer";
		String formatKey = "%s_value_%09X";
		String formatValue = "%s_value_%09X";
		JedisPoolConfig config = new JedisPoolConfig();
		JedisPool pool = new JedisPool(config, host, port, 0, "foobared");

		for (int i = 0; i < 30000000; i++) {
			System.out.println(i);
			Jedis j = pool.getResource();
			j.set(String.format(formatKey, key, i),
					String.format(formatValue, key, i));
			pool.returnResource(j);
		}
	}

	public static void main(String[] args) {
		final int threadCount = 1000;// 测试线程数
		final int runCount = 50000;// 每个线程重复的请求的次数
		final String host = "192.168.1.151";
		final int port = 6379;

		JedisPoolConfig config = new JedisPoolConfig();
		// config.setMaxIdle(threadCount);
		config.setMaxTotal(threadCount);
		JedisPool pool = new JedisPool(config, host, port, 0, "foobared");
		for (int i = 0; i < threadCount; i++) {
			new TestThread(i, runCount, pool).start();
		}
	}

	public static class TestThread extends Thread {
		private final int index;
		private final int runCount;
		private JedisPool pool;

		public TestThread(int index, int runCount, JedisPool pool) {
			this.index = index;
			this.runCount = runCount;
			this.pool = pool;
		}

		@Override
		public void run() {
			long start = System.currentTimeMillis(), end;
			// tsetSignSet();
			testSignGet();
			end = System.currentTimeMillis();
			System.err.println(String.format("thread_%03X used time : %d",
					index, (end - start)));
		}

		String key = "lucifer";
		String format = "%s_%03X_value_%09X";
		String format2 = "%03X_value_%09X";

		private void tsetSignSet() {
			Jedis j = pool.getResource();
			String str;
			for (int i = 0; i < runCount; i++) {
				// str = String.format(format, key, index, i);
				str = String.format(format2, index, i);
				// j.set(str, str);
				j.hset(key, str, str);
				System.out.println(str);
			}
			pool.returnResource(j);
		}

		private void testSignGet() {
			Jedis j = pool.getResource();
			String str, value;
			for (int i = 0; i < runCount; i++) {
				// str = String.format(format, key, index, i);
				str = String.format(format2, index, i);
				// j.set(str, str);
				// j.hset(key, str, str);
				value = j.hget(key, str);
				System.out.println(str + " : " + value);
			}
			pool.returnResource(j);
		}

	}
}
