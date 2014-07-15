/**
 * 
 */
package cn.lucifer.demo.clazz;

/**
 * @author Lucifer
 * 
 */
public abstract class A implements Cloneable {
	public int a = 1;
	private int i = 1;

	static{
		System.out.println("A");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @param i
	 *            the {@link #i} to set
	 */
	public void setI(int i) {
		this.i = i;
	}

	/**
	 * @return the {@link #i}
	 */
	public int getI() {
		return i;
	}

}
