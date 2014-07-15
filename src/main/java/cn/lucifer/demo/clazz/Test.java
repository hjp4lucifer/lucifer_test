/**
 * 
 */
package cn.lucifer.demo.clazz;

import com.google.gson.Gson;

/**
 * @author Lucifer
 * 
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		trace(new B());
//		clone(new B());
		System.out.println(new B());
//		System.out.println(new AS().a);
	}

	public static void trace(A a) {
		Class clazz = a.getClass();
		System.out.println(clazz);
	}
	
	public static void clone(A a){
		try {
			System.out.println(new Gson().toJson(a.clone()));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
}
