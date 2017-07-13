package cn.lucifer.demo.json;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class FastJsonTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testString() {
		String s = "abcd\"'rest";
		System.out.println(JSON.toJSONString(s));
	}

}
