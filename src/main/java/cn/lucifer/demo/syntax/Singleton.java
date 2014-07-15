package cn.lucifer.demo.syntax;

public class Singleton {

	// 增加自己的静态实例变量
	static Singleton x = new Singleton();

	static String name = "rjx";

	static {
		System.out.println("---------运行静态块的代码------------");
		name = "Jam";
		//x = new Singleton();
	}

	{
		System.out.println("--------运行实例块/初始化块的代码--------");
	}

	public Singleton() {
		System.out.println("--------运行构造函数的代码-------");
		System.out.println("静态变量name现在的值是" + name);
	}

	public static void sayHi(){
		System.out.println("-------------- say hi -------------- " + x);
	}
}
