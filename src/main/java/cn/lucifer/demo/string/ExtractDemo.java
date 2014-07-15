/**
 * 
 */
package cn.lucifer.demo.string;

import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class ExtractDemo {

	@Test
	public void testMain() {
		String source = "document.write(\" <p align=center style='FONT-SIZE:13.5pt;font-family:宋体'><b><a name='18_8'>第二部【都市游：之傲龙吟】 第四十八章 战士心(下)</b><p>\");";
		source = source
				.replace(
						"document.write(\" <p align=center style='FONT-SIZE:13.5pt;font-family:宋体'><b>",
						"");
		source = source.replace("</b><p>\");", "");
		source = source.replaceAll("<a name=\\'\\d{1,2}_\\d{1}\\'>", "");
		System.out.println(source);
	}

}
