package cn.lucifer.demo.qq;

public class Convert {
	public static byte ToByte(String value, int fromBase) {
		int num;
		if (fromBase == 2) {
		} else if (fromBase == 8) {
		} else if (fromBase == 10) {
		} else if (fromBase == 0x10) {
		} else {
			throw new RuntimeException("Arg_InvalidBase");
		}
		num = ParseNumbers.StringToInt(value, fromBase, 0x1200);
		if (num < 0) {
			throw new RuntimeException("Overflow_Byte");
		}
		if (num <= 0xff) {
			return (byte) num;
		}

		throw new RuntimeException("unknow ToByte");
	}

	public static long ToInt64(String value, int fromBase) {
		if (fromBase == 2) {
		}
		if (fromBase == 8) {
		}
		if (fromBase == 10) {
		}
		if (fromBase == 0x10) {
		} else {
			throw new RuntimeException("Arg_InvalidBase");
		}
		return ParseNumbers.StringToLong(value, fromBase, 0x1000);
	}

}
