package cn.lucifer.demo.smali.parse;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 关键字字典工具
 * 
 * @see http://blog.sina.com.cn/s/blog_6940a6780102yg4u.html
 * @author Lucifer
 *
 */
public class KeywordDictionaryUtils {

	/**
	 * key=基本数据类型flag, vlaue=基本数据类型
	 */
	public final static HashMap<String, String> flag_2_base_type = new HashMap<>();
	/**
	 * @see #flag_2_base_type
	 */
	public final static HashMap<String, String> base_type_2_flag = new HashMap<>();

	static {
		flag_2_base_type.put("B", "byte");
		flag_2_base_type.put("C", "char");
		flag_2_base_type.put("D", "double");
		flag_2_base_type.put("F", "float");
		flag_2_base_type.put("I", "int");
		flag_2_base_type.put("S", "short");
		flag_2_base_type.put("V", "void");
		flag_2_base_type.put("J", "long");
		flag_2_base_type.put("Z", "boolean");
		reverse(flag_2_base_type, base_type_2_flag);

	}

	private static <K, V> void reverse(HashMap<K, V> from, HashMap<V, K> to) {
		for (Entry<K, V> entry : from.entrySet()) {
			to.put(entry.getValue(), entry.getKey());
		}
	}
}
