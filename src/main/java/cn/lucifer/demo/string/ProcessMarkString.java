package cn.lucifer.demo.string;

import org.junit.Test;

public class ProcessMarkString {
	@Test
	public void testMain() {
		String string = "\"2159925940\"";
		string = string.replaceAll("\"", "");
		System.out.println(string);

		long id = Long.parseLong(string);
		System.out.println(id + " == ");
	}
}
