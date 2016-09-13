package cn.lucifer.demo.qq;

public class ParseNumbers {
	public static int StringToInt(String s, int radix, int flags)
	{
		return Integer.parseInt(s, radix);
	    //return StringToInt(s, radix, flags, (IntPtr) 0);
	}

	public static long StringToLong(String s, int radix, int flags)
	{
		return Long.parseLong(s, radix);
	    //return StringToLong(s, radix, flags, (IntPtr) 0);
	}

}
