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

	@Test
	public void testSwitch(){
		int type = 7;
		switch (type) {
		case 7:
			System.out.println(type + " -");
			break;
		case 0:
		case 8:
			System.out.println(type + " --");
			break;

		default:
			System.out.println("default");
			break;
		}
	}
}
