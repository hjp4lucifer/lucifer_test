package cn.lucifer.demo.des;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.junit.Test;

/***
 * @Description: des加密/解密类
 * @author zhouya
 * @version V1.0
 * 
 */
public class JiujunDes {

	static SecretKeyFactory skeyFactory;
	static {
		try {
			skeyFactory = SecretKeyFactory.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 加密（使用DES算法）
	 * 
	 * @param txt
	 *            需要加密的文本
	 * @param key
	 *            密钥
	 * @return 成功加密的文本
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String enCrypto(String key, String txt)
			throws InvalidKeySpecException, InvalidKeyException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		long start;
		start = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
		// SecretKeyFactory skeyFactory = null;
		Cipher cipher = null;

		System.out.println("a : " + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();
		try {
			// skeyFactory = SecretKeyFactory.getInstance("DES");

			System.out.println("b1 : " + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			cipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		System.out.println("b : " + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();

		SecretKey deskey = skeyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] cipherText = cipher.doFinal(txt.getBytes());

		System.out.println("c : " + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();

		for (int n = 0; n < cipherText.length; n++) {
			String stmp = (java.lang.Integer.toHexString(cipherText[n] & 0XFF));

			if (stmp.length() == 1) {
				sb.append("0" + stmp);
			} else {
				sb.append(stmp);
			}
		}

		System.out.println("d : " + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();

		String s = sb.toString().toUpperCase();

		System.out.println("a : " + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();

		return s;
	}

	/**
	 * 解密（使用DES算法）
	 * 
	 * @param txt
	 *            需要解密的文本
	 * @param key
	 *            密钥
	 * @return 成功解密的文本
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String deCrypto(String key, String txt)
			throws InvalidKeyException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
		SecretKeyFactory skeyFactory = null;
		Cipher cipher = null;
		try {
			skeyFactory = SecretKeyFactory.getInstance("DES");
			cipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		SecretKey deskey = skeyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.DECRYPT_MODE, deskey);
		byte[] btxts = new byte[txt.length() / 2];
		for (int i = 0, count = txt.length(); i < count; i += 2) {
			btxts[i / 2] = (byte) Integer.parseInt(txt.substring(i, i + 2), 16);
		}
		return (new String(cipher.doFinal(btxts)));
	}

	@Test
	public void testMain() throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		// String soureTxt = "这里是要加密的文本内容";
		String soureTxt = "{\"f\":\"game.card.dizhu\",\"g\":\"插屏测试1标题\",\"d\":\"千千游斗地主\",\"e\":1,\"b\":\"http://cdn.ninebox.cn/download/apks/QQY/QQY_15_9.apk\",\"c\":1,\"a\":\"插屏测试1内容\",\"n\":\"2013-07-26\",\"o\":\"中文\",\"l\":\"http://cdn.ninebox.cn/download/apks/QQY/mtxx01.jpg;http://cdn.ninebox.cn/download/apks/QQY/mtxx02.jpg\",\"m\":\"\",\"j\":\"1.1.2.2\",\"k\":\"http://cdn.ninebox.cn/download/apks/QQY/iconfuben72.png\",\"h\":\"应用\",\"i\":8397,\"t\":120,\"s\":0,\"r\":\"http://cdn.ninebox.cn/download/advert/chapingceshi1biaoti/31.png;http://cdn.ninebox.cn/download/advert/chapingceshi1biaoti/32.png\",\"q\":3,\"p\":1}]";

		String key = "jiujunbox";
		String str = null;
		System.out.println("明文：" + soureTxt);
		str = enCrypto(key, soureTxt);
		System.out.println("加密：" + str);
		System.out
				.println("解密："
						+ deCrypto(
								"jiujunjiujun",
								"144081FAE38E8352CCB51D1BACED0E97253E26BEAD5690676C0FC807DC3B627A183874E831603A00B795A4C6850401D8AD32FC4E3E0972D62C15D65AC21294940757BF25312AF7237E4C4B27275D146EF96B353354923E2AC7805DB0E83B0CF8ADBDA9BC9CD1FD638A102992A66A952EB0377942E6D9EF76F5994E91CFD62F4C2A48C572863B18B1F205AA241A1EF9BD318E09D3CFE21975A3FE8850DEE37B5109193C817AC3EC1820DB5EE5A3388039D4E82368BD1A765763F0D0F5483A127D9539421FEA68887DE760B20CA880C742FD415EC448B132EA53D041D04A66CA1692964530BCE6E6E1"));
		System.out.println(str + "====" + key);

	}
}