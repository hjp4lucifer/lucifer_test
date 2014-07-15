package cn.lucifer.demo.des;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Test;

/**
 * des加密算法测试
 * 
 * @author Lucifer
 * 
 */
public class DesTest {

	String soureTxt = "{\"f\":\"game.card.dizhu\",\"g\":\"插屏测试1标题\",\"d\":\"千千游斗地主\",\"e\":1,\"b\":\"http://cdn.ninebox.cn/download/apks/QQY/QQY_15_9.apk\",\"c\":1,\"a\":\"插屏测试1内容\",\"n\":\"2013-07-26\",\"o\":\"中文\",\"l\":\"http://cdn.ninebox.cn/download/apks/QQY/mtxx01.jpg;http://cdn.ninebox.cn/download/apks/QQY/mtxx02.jpg\",\"m\":\"\",\"j\":\"1.1.2.2\",\"k\":\"http://cdn.ninebox.cn/download/apks/QQY/iconfuben72.png\",\"h\":\"应用\",\"i\":8397,\"t\":120,\"s\":0,\"r\":\"http://cdn.ninebox.cn/download/advert/chapingceshi1biaoti/31.png;http://cdn.ninebox.cn/download/advert/chapingceshi1biaoti/32.png\",\"q\":3,\"p\":1}]";

	String soureTxt2 = "abcde";

	String key = "jiujunjiujun";
	String code = "144081FAE38E8352CCB51D1BACED0E97253E26BEAD5690676C0FC807DC3B627A183874E831603A00B795A4C6850401D8AD32FC4E3E0972D62C15D65AC21294940757BF25312AF7237E4C4B27275D146EF96B353354923E2AC7805DB0E83B0CF8ADBDA9BC9CD1FD638A102992A66A952EB0377942E6D9EF76F5994E91CFD62F4C2A48C572863B18B1F205AA241A1EF9BD318E09D3CFE21975A3FE8850DEE37B5109193C817AC3EC1820DB5EE5A3388039D4E82368BD1A765763F0D0F5483A127D9539421FEA68887DE760B20CA880C742FD415EC448B132EA53D041D04A66CA1692964530BCE6E6E1";

	@Test
	public void testJiujunEnCrypto() throws InvalidKeyException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		String str = JiujunDes.enCrypto(key, soureTxt);
		System.out.println(str);

		str = JiujunDes.enCrypto(key, soureTxt2);
		System.out.println(str);
	}

	@Test
	public void testJiujunDeCrypto() throws InvalidKeyException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		for (int i = 0; i < 115; i++) {
			long start = System.currentTimeMillis(), end;
			String str = JiujunDes.deCrypto(key, code);
			end = System.currentTimeMillis();
			System.out.println("de time: " + (end - start));
			System.out.println(str);
		}
	}

	@Test
	public void testDesEncode() throws Exception {
		String str = new DESCodec().encrypt(soureTxt, key);
		System.out.println(str);
	}

	@Test
	public void testDeesEncode2() {
		String str = new DES().encrypt(key, soureTxt);
		System.out.println(str);
	}
}
