package cn.lucifer.demo.syntax;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OtherTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOr() {
		boolean bln = false;
		bln |= false; 
		System.out.println(bln);
		bln |= true; 
		System.out.println(bln);
		bln |= false; 
		System.out.println(bln);
		bln |= true; 
		System.out.println(bln);
	}

}
