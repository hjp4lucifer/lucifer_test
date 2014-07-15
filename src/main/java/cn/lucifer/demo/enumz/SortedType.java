/**
 * 
 */
package cn.lucifer.demo.enumz;

/**
 * 一些基本的排序类型(in morphia)
 * 
 * @author Lucifer
 * 
 */
public enum SortedType {

	CREATE_ASC("create"), CREATE_DESC("-create");

	private String value;

	private SortedType(String val) {
		value = val;
	}

	private boolean equals(String val) {
		return value.equals(val);
	}

	public String val() {
		return value;
	}

	public static SortedType fromString(String val) {
		for (int i = 0; i < values().length; i++) {
			SortedType fo = values()[i];
			if (fo.equals(val))
				return fo;
		}
		return null;
	}

}
