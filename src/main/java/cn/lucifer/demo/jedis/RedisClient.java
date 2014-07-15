package cn.lucifer.demo.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient {

	public static Logger log = Logger.getLogger(RedisClient.class);

	protected final JedisPool pool;

	protected final String host;

	protected final int port;

	public RedisClient(final String host, final int port, final int threadCount) {
		// JedisPoolConfig config = new JedisPoolConfig();
		// config.setMaxActive(threadCount);
		this.host = host;
		this.port = port;
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(threadCount);
		pool = new JedisPool(config, host, port, 0);
	}

	protected void operateErrorLog(String optName, String key, String value,
			Exception e) {
		log.error(String.format("%s:%d error, %s %s %s", host, port, optName,
				key, value), e);
	}

	public void set(String key, String value) {
		Jedis jedis = getClient();
		try {
			jedis.set(key, value);
		} catch (RuntimeException e) {
			operateErrorLog("set", key, value, e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public void setex(final String key, final int seconds, final String value) {
		Jedis jedis = getClient();
		try {
			jedis.setex(key, seconds, value);
		} catch (RuntimeException e) {
			operateErrorLog("setex", key, value, e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public String get(String key) {
		Jedis jedis = getClient();
		try {
			return jedis.get(key);
		} catch (RuntimeException e) {
			operateErrorLog("get", key, null, e);
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public void del(String key) {
		Jedis jedis = getClient();
		try {
			jedis.del(key);
		} catch (RuntimeException e) {
			operateErrorLog("del", key, null, e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public Boolean exists(String key) {
		Jedis jedis = getClient();
		try {
			return jedis.exists(key);
		} catch (RuntimeException e) {
			operateErrorLog("exists", key, null, e);
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public void hset(String key, String field, String value) {
		Jedis jedis = getClient();
		try {
			jedis.hset(key, field, value);
		} catch (RuntimeException e) {
			operateErrorLog("hset", key + ' ' + field, value, e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public String hget(String key, String field) {
		Jedis jedis = getClient();
		try {
			return jedis.hget(key, field);
		} catch (RuntimeException e) {
			operateErrorLog("hget", key, field, e);
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public void hdel(String key, String fields) {
		Jedis jedis = getClient();
		try {
			jedis.hdel(key, fields);
		} catch (RuntimeException e) {
			operateErrorLog("hdel", key, fields, e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public Long incrBy(String key, long integer) {
		Jedis jedis = getClient();
		try {
			return jedis.incrBy(key, integer);
		} catch (RuntimeException e) {
			operateErrorLog("incrBy", key, String.valueOf(integer), e);
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public Long decrBy(String key, long integer) {
		Jedis jedis = getClient();
		try {
			return jedis.decrBy(key, integer);
		} catch (RuntimeException e) {
			operateErrorLog("decrBy", key, String.valueOf(integer), e);
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 使用完后需要调用{@link #returnResource()}
	 * 
	 * @return
	 */
	public Jedis getClient() {
		try {
			return pool.getResource();
		} catch (RuntimeException e) {
			log.error(String.format("Connection to Redis failed by %s:%d",
					host, port));
			throw e;
		}
	}

	public void returnResource(Jedis jedis) {
		pool.returnResource(jedis);
	}
}
