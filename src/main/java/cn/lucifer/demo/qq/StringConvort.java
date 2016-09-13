package cn.lucifer.demo.qq;

public class StringConvort {
	public static String GetMiddleText(String allText, String firstText,
			String lastText) {
		int num;
		int num2;
		String str;
		try {
			num = allText.indexOf(firstText) + firstText.length();
			num2 = allText.indexOf(lastText, num);
			str = allText.substring(num, num2 - num);
		} catch (Exception e) {
			str = "null";
		}
		return str;
	}
}
