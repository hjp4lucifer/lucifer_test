package cn.lucifer.demo.qq;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

public class PCClass {

	public void Login0836() {

	}

	public void Login0836a(long num) {
		String str3 = null;
		String str4 = str3.substring(0, 2);
		if (!(str4.equals("FC"))) {
			System.out.println("数据库中没有找到这样的值");
			return;
		}

		String str6 = Unit.GetQQNum(StringConvort.GetMiddleText(str3,
				"00 05 00 06 00 01", "01 13 00 20"));

		String rsp = StringUtils.join(new Object[] { "获取完成\r\n", num, "----",
				str6, "\r\n" });
		System.out.println(rsp);
	}

	public byte[] GetDeCodeByte(byte[] bytes) {
		byte[] buffer;
		String str;
		int num;
		buffer = bytes;
		str = Unit.TileHexString(buffer);
		if (str.indexOf("03 00 00 00 00 00") <= 0) {
		} else {
			num = str.indexOf("03 00 00 00 00 00");
			str = str.substring(0, num + 2);
		}

		// str = str.Remove(0, GetUinLength(14) + 1).Trim();
		str = str.substring(GetUinLength(14) + 1).trim();
		return Unit.HexStringToByteArray(new StrBuilder(str)
				.delete(str.length() - 2, str.length()).toString().trim());
	}

	public static int GetUinLength(int int32) {
		return ((int32 * 2) + (int32 - 1));
	}

}
