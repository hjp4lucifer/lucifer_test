package cn.lucifer.demo.string;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ocpsoft.prettytime.PrettyTime;

public class PrettytimeTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		PrettyTime p = new PrettyTime(Locale.CHINESE);
		System.out.println(p.format(new Date(
				System.currentTimeMillis() - 3600000 * 24)));
		// prints: “moments from now”

		System.out.println(p.format(new Date(
				System.currentTimeMillis() + 1000 * 60 * 10)));
		// prints: “10 minutes from now”
	}

}
