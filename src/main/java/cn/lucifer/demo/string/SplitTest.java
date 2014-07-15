/**
 * 
 */
package cn.lucifer.demo.string;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class SplitTest {

	@Test
	public void testMain() {
		// String str = "3.12.2";
		// String[] nums = str.split("\\.");
		// System.out.println(nums.length);
		// for (String n : nums) {
		// System.out.println(n);
		// }
		String skillDescription = "常に機動力+22.6%アップするスキル";
		System.out.println(getPassiveSkillUpValue(skillDescription));
	}

	public int getPassiveSkillUpValue(String skillDescription) {
		String prefix = "常に機動力+";
		String suffix = "%アップするスキル";
		String value = skillDescription.substring(
				skillDescription.indexOf(prefix) + prefix.length(),
				skillDescription.indexOf(suffix));
		return (int) Float.parseFloat(value);
	}

	@Test
	public void test2() {
		String s = "SELECT p.coo_id,p.name,s.is_push,p.sdk_version,p.status,p.publish_time,p.icon,p.folder,p.id,s.is_push_video ,s.is_push_list, p.status_video, p.status_kuzai, ";
		String[] array = s.split(",");
		System.out.println(array.length);
	}

	@Test
	public void test3() {
		String[] array = StringUtils.split("a,bc,d,e,f", ',');
		for (String str : array) {
			System.out.println(str);
		}
	}

	@Test
	public void test5() {
		String s = "6,5,3;6,3";
		for (String s1 : s.split(";")) {
			System.out.println(s1.split(",").length);
		}
	}

	@Test
	public void test6() {
		String s = ";;;3;";
		String[] array = s.split(";");
		for (String str : array) {
			System.out.println(str);
		}
	}

	@Test
	public void testDoubleSplit() {
		String s = "1001|xxxxxx,1002|,1003|bbbbbb";
		s = "1001|xxxxxx,|xxxxaa,1003|bbbbbb";
		String[] lv1Array = s.split(",");
		for (String lv1 : lv1Array) {
			String[] lv2 = lv1.split("\\|");
			System.out.print(lv2[0]);
			System.out.println(" : " + lv2.length);
		}
	}
}
