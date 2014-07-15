/**
 * 
 */
package cn.lucifer.demo.clazz;

/**
 * @author Lucifer
 * 
 */
public class B extends A {

	private int j = 2;
	static {
		System.out.println("B");
		AS.say();
	}

	/**
	 * @param j
	 *            the {@link #j} to set
	 */
	public void setJ(int j) {
		this.j = j;
	}

	/**
	 * @return the {@link #j}
	 */
	public int getJ() {
		return j;
	}

}
