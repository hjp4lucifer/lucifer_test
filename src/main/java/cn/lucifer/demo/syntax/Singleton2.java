package cn.lucifer.demo.syntax;

public class Singleton2 {
	private static Singleton2 singleton2 = new Singleton2();

	private Singleton2() {
		System.out.println("init Singleton2 ");
	}
	
	public static Singleton2 getInstance(){
		return singleton2;
	}
}
