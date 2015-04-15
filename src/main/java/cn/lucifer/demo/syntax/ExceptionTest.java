package cn.lucifer.demo.syntax;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExceptionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		try {
			System.out.println(1);
			throw new Exception("aaaa");
		} catch (Exception e) {
			System.out.println(2);
			throw e;
		} finally {
			System.out.println(3);
		}
	}

}
