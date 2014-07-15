package cn.lucifer.demo.syntax.ex;

import cn.lucifer.demo.syntax.Singleton2;

public class TestSingleton {
	public static void main(String[] args) throws Exception {
		// Singleton singleton = new Singleton();
		// singleton.sayHi();
		Class<Singleton2> clazz = Singleton2.class;
		System.out.println("------1------");
		// Class.forName("cn.lucifer.demo.syntax.Singleton2");
		System.out.println("------2------");
		// Singleton2.getInstance();
		System.out.println("------3------");
	}
}
