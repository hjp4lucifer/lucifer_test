package cn.lucifer.demo.des;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

/**
 * DES对称加密算法
 * 
 * @see 
 *      ==========================================================================
 *      =======================================
 * @see 对称加密算法就是能将数据加解密。加密的时候使用密钥对数据进行加密，解密的时候使用同样的密钥对数据进行解密
 * @see DES是美国国家标准研究所提出的算法。由于加解密的数据安全性和密钥长度成正比，故DES的56位密钥已经形成安全隐患
 * @see 后来针对DES算法进行了改进，有了三重DES算法（也称DESede或Triple-DES）。全名是TDEA：Triple Data
 *      Encryption Algorithm
 * @see DESede针对DES算法的密钥长度较短以及迭代次数偏少问题做了相应改进，提高了安全强度
 * @see 不过DESede算法处理速度较慢，密钥计算时间较长，加密效率不高等问题使得对称加密算法的发展不容乐观
 * @see 
 *      ==========================================================================
 *      =======================================
 * @see Java和BouncyCastle针对DES算法的数据加密支持是不同的，主要体现在密钥长度、工作模式以及填充方式上
 * @see Java6只支持56位密钥，而BouncyCastle支持64位密钥，它的官网是http://www.bouncycastle.org/
 * @see 即便是在DESede算法上，BouncyCastle的密钥长度也要比Java的密钥长度长
 * @see 
 *      ==========================================================================
 *      =======================================
 * @see 另外，Java的API中仅仅提供了DES、DESede、PBE三种对称加密算法密钥材料实现类
 * @see 
 *      ==========================================================================
 *      =======================================
 * @see 关于Java加解密的更多算法实现
 *      ，可以参考此爷的博客http://blog.csdn.net/kongqz/article/category/800296
 * @see 
 *      ==========================================================================
 *      =======================================
 */
public class DESCodec {
	// 算法名称
	public final String KEY_ALGORITHM = "DES";
	// 算法名称/加密模式/填充方式
	// DES共有四种工作模式-->>ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
	public final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

	/**
	 * 生成密钥
	 */
	public String initkey() throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM); // 实例化密钥生成器
		kg.init(56); // 初始化密钥生成器
		SecretKey secretKey = kg.generateKey(); // 生成密钥
		return Base64.encodeBase64String(secretKey.getEncoded()); // 获取二进制密钥编码形式
	}

	/**
	 * 转换密钥
	 */
	private Key toKey(byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key); // 实例化Des密钥
		SecretKeyFactory keyFactory = SecretKeyFactory
				.getInstance(KEY_ALGORITHM); // 实例化密钥工厂
		SecretKey secretKey = keyFactory.generateSecret(dks); // 生成密钥
		return secretKey;
	}

	/**
	 * 加密数据
	 * 
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @return 加密后的数据
	 */
	public String encrypt(String data, String key) throws Exception {
		Key k = toKey(Base64.decodeBase64(key)); // 还原密钥
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM); // 实例化Cipher对象，它用于完成实际的加密操作
		cipher.init(Cipher.ENCRYPT_MODE, k); // 初始化Cipher对象，设置为加密模式
		return Base64.encodeBase64String(cipher.doFinal(data.getBytes())); // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
		// return new String(cipher.doFinal(data.getBytes()));
	}

	/**
	 * 解密数据
	 * 
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return 解密后的数据
	 */
	public String decrypt(String data, String key) throws Exception {
		Key k = toKey(Base64.decodeBase64(key));
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k); // 初始化Cipher对象，设置为解密模式
		return new String(cipher.doFinal(Base64.decodeBase64(data))); // 执行解密操作
	}

	@Test
	public void testMain() throws Exception {
		String source = "岂向苍天";
		System.out.println("原文: " + source);

		String key = initkey();
		System.out.println("密钥: " + key);

		String encryptData = encrypt(source, key);
		System.out.println("加密: " + encryptData);

		String decryptData = decrypt(encryptData, key);
		System.out.println("解密: " + decryptData);
	}
}
