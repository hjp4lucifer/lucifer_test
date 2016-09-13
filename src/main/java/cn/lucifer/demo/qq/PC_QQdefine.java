package cn.lucifer.demo.qq;

public class PC_QQdefine {
	// Fields
	public static byte[] DecodeKey;
	public final String FixedDate1 = "00 18 00 16 00 01";
	public final String FixedDate2 = "00 00 04 4B 00 00 00 01 00 00 15 09 ";
	public static byte[] HDKey;
	public static byte[] key0825;
	public static byte[] key08261;
	public static byte[] key08262;
	public static String LoginIP;
	public static int PacketTimeout;
	public static int ResendMaxSize;
	public static byte[] token255;
	public static byte[] token255pwd;
	public final String VersionCode = "03 00 00 00 01 01 01 00 00 67 42";
	public final String VersionCode2 = "";
	public final String VersionNum = "35 55";

	// Methods
	static {
		DecodeKey = Unit
				.HexStringToByteArray("13 D9 24 CA 5E 04 69 D2 84 EF FE A8 7A 5A 5F 1C");
		HDKey = Unit
				.HexStringToByteArray("02 51 CA 4A AB 66 E8 0A E4 D2 79 92 1A CE 3C 3D FE E2 37 88 15 1F 45 36 8D");
		key0825 = Unit
				.HexStringToByteArray("77 92 39 4F 1A FD 3B BF A9 00 6B C8 07 BC F2 3B");
		key08261 = Unit
				.HexStringToByteArray("22 E1 CE BD 3D 86 3F 87 C5 78 0A 7F 0D 50 B6 25");
		key08262 = Unit
				.HexStringToByteArray("6D 47 53 5A 5A 57 3D 48 72 77 2C 2D 36 71 7A 76");
		LoginIP = "183.60.56.100";
		PacketTimeout = 15;
		ResendMaxSize = 4;
	}

	public PC_QQdefine() {
		return;
	}

	public static int GetUShort(byte[] HexByte) {
		int num;
		num = ((HexByte[0] << 8) | HexByte[1]);
		return num;
	}

	// Nested Types
	public static class Command {
		public static int send0002 = 2, send00ba = 0xba, send00ba1 = 0xba1,
				send00ec = 0xec, send0314 = 0x314, send0825 = 0x825,
				send0828 = 0x828, send0836 = 0x836;
	}
}
