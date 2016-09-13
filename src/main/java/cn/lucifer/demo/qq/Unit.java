package cn.lucifer.demo.qq;

public class Unit {
	public static String TileHexString(byte[] Byte) {
		return TileHexString(Byte, 0, (int) Byte.length);
	}

	public static String TileHexString(byte[] Byte, int Offset, int Length) {
		StringBuilder builder;
		int num;
		builder = new StringBuilder();
		num = Offset;

		while (num < (Offset + Length)) {
			builder.append(String.format("%02X ", (byte) Byte[num]));
			num += 1;
		}
		return builder.toString().toUpperCase();

	}

	public static byte[] HexStringToByteArray(String hexString) {
		byte[] buffer;
		int num;
		hexString = hexString.replace(" ", "").replace("\n", "");
		if ((hexString.length() % 2) == 0) {
			buffer = new byte[hexString.length() / 2];
			num = 0;
		} else {
			hexString = hexString + " ";
			buffer = new byte[hexString.length() / 2];
			num = 0;
		}

		while (num < ((int) buffer.length)) {
			buffer[num] = Convert.ToByte(hexString.substring(num * 2, 2), 0x10);
			num += 1;
		}

		return buffer;
	}

	public static String GetQQNum(String six) {
		long num;
		return String.valueOf(Convert.ToInt64(six.replace(" ", ""), 0x10));
	}

}
