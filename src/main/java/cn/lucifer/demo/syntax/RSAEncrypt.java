package cn.lucifer.demo.syntax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;

/**
 * 私密圈加/解密
 * 
 * @author Lucifer
 *
 */
public class RSAEncrypt {

	public static Log logger = LogFactory.getLog(RSAEncrypt.class);

	private static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJN7qi6yEcE+c4MPAatZhPfXGr\r"
			+ "JBVS28tZ4L/XquZlyl3BqUjECWqaLIpOFrJrmF48MC2Porw7KlnjxfNdIgjHBLOi\r"
			+ "561xK8r5IbGxOQm0udML6wR0qxvnd9eTv/Wbb7movQH9teKKfSc6FOzfLYAVz91s\r"
			+ "iAiV+KH3dTMGNIeY6wIDAQAB\r";

	private static final String DEFAULT_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMk3uqLrIRwT5zgw\r"
			+ "8Bq1mE99caskFVLby1ngv9eq5mXKXcGpSMQJaposik4WsmuYXjwwLY+ivDsqWePF\r"
			+ "810iCMcEs6LnrXEryvkhsbE5CbS50wvrBHSrG+d315O/9Ztvuai9Af214op9JzoU\r"
			+ "7N8tgBXP3WyICJX4ofd1MwY0h5jrAgMBAAECgYEApnV76JsrYNb2SP3zI6Vmwxu7\r"
			+ "lSDcoxdBOrE7A2dWAo9O0I8Cq2o+zhxarg8Iuwnpq1EP3+t4XFnE0zFHCpzFNKYg\r"
			+ "0bU5a4M+YPpgG/frv/USko1JoKzMVOVcJsxpz5mWb71yJod51hS0JWRzw5nDfHCI\r"
			+ "PVEBiOoMbJEEhKeBuIECQQD4OUFUMwpfN/mZVZsjGgH8zS7Tg/xdORen9jLpgDj8\r"
			+ "+ThgRQJnErOuK8+N5PxMOIJIKT/30qmdMFXDvQ1ie2aJAkEAz4V829cFnsg9oYM6\r"
			+ "k3ahZVXpB4DbeHjrweYUmv+CFmbLAOYP2YTxclkDIotXp1Zaz6SLov16hlSz3/6J\r"
			+ "+Fvm0wJBAMCEP4vbTkcNddOb1ofB6xqz1IaODlQLLLLDOzdokEp7zGK2AygIFD2Y\r"
			+ "kxB9McS36yumeott4skcLNh0LwZbqOkCQET6gf4xAtU8if/dtuK9U/hzDfpIgqsQ\r"
			+ "xoy1BbRfqcX4dRHfZvVOk1MG81vQJVqiAZ1zfFa5Grj7/q491W6QXbECQQDLkZmk\r"
			+ "FBh/L7kRAfJUkjm8nHuUNzuri1t93oEiPm1dPmIDg+Sbewx2g/Mi2UTYGFKSrvsv\r"
			+ "BMQaaCrxlEUFUQ1H\r";

	// private static final String RSA_SERVICE_URL =
	// PropertiesHolder.get("rsaurl", "http://q.duowan.com/special/index.php");

	static {

		// 加载公钥
		try {
			RSAEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY);
			System.out.println("加载公钥成功:" + RSAEncrypt.DEFAULT_PUBLIC_KEY);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("加载公钥失败");
		}

		// 加载私钥
		try {
			RSAEncrypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY);
			System.out.println("加载私钥成功:" + RSAEncrypt.DEFAULT_PRIVATE_KEY);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("加载私钥失败");
		}
	}

	/**
	 * 私钥
	 */
	private static RSAPrivateKey privateKey;

	/**
	 * 公钥
	 */
	private static RSAPublicKey publicKey;

	/**
	 * 字节数据转字符串专用集合
	 */
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 获取私钥
	 * 
	 * @return 当前的私钥对象
	 */
	public static RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * 获取公钥
	 * 
	 * @return 当前的公钥对象
	 */
	private static RSAPublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * 随机生成密钥对
	 */
	private void genKeyPair() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair = keyPairGen.generateKeyPair();
		this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
		this.publicKey = (RSAPublicKey) keyPair.getPublic();
	}

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param in
	 *            公钥输入流
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	private static void loadPublicKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			System.out.println(sb.toString());
			loadPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	private static void loadPublicKey(String publicKeyStr) throws Exception {
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new Exception("公钥非法");
		} catch (IOException e) {
			throw new Exception("公钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从文件中加载私钥
	 * 
	 * @param keyFileName
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	private static void loadPrivateKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	private static void loadPrivateKey(String privateKeyStr) throws Exception {
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new Exception("私钥非法");
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	private static BouncyCastleProvider bouncyCastleProvider4Encrypt = new BouncyCastleProvider();

	/**
	 * 加密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	private static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}

		try {
			Cipher cipher = Cipher.getInstance("RSA",
					bouncyCastleProvider4Encrypt);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(plainTextData);
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	private static BouncyCastleProvider bcProvider4Decrypt = new BouncyCastleProvider();

	/**
	 * 解密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}

		try {
			Cipher cipher = Cipher.getInstance("RSA", bcProvider4Decrypt);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(cipherData);
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new Exception("无此解密算法");
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		}
	}

	/**
	 * 字节数据转十六进制字符串
	 * 
	 * @param data
	 *            输入数据
	 * @return 十六进制内容
	 */
	private static String byteArrayToString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
			// 取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
		}
		return stringBuilder.toString();
	}

	/**
	 * 十六进制字符串转字节数据
	 * 
	 * @param data
	 *            十六进制内容
	 * @return
	 */
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * 加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String encryptParam(String plainText) {
		if (StringUtils.isBlank(plainText))
			return "";
		try {
			String result = byteArrayToString(encrypt(
					RSAEncrypt.getPublicKey(), plainText.getBytes()));
			// logger.info("ENCRYPTPARAM OK.");
			return result;
		} catch (Exception e) {
			logger.error("ENCRYPTPARAM FAIL.系统出现异常,加密文件不成功，明文是：" + plainText
					+ ",原因：", e);
		}
		return "";

		// try {
		// return NetUtil.getHttpRequest("r=api/encrypt&string="+plainText,
		// RSA_SERVICE_URL, 5);
		// } catch (BaseCheckedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// return null;
	}

	/**
	 * 解密
	 */
	public static String decryptParam(String paramByRsa) {
		if (StringUtils.isBlank(paramByRsa))
			return "";
		try {
			String result = new String(decrypt(RSAEncrypt.getPrivateKey(),
					hexStringToByteArray(paramByRsa)));
			// logger.info("DECRYPTPARAM OK.");
			return result;
		} catch (Exception e) {
			logger.error("DECRYPTPARAM FAIL.系统出现异常,解密文件不成功,密文是：" + paramByRsa
					+ "，原因：", e);
		}
		return "";

		// try {
		// return NetUtil.getHttpRequest("r=api/decrypt&string="+paramByRsa,
		// RSA_SERVICE_URL, 5);
		// } catch (BaseCheckedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// return null;
	}

	public static void main(String[] args) throws Exception {

		String params = "3f5e151da2adbd51b8518033dfdca4751d247d4e9ef788bdb1afba6e01c0bdc62b70cca12219bf1765ac542679fd1e86dae9aaba56d54141079e1125c8c9d3c4f6775ca2cf184c354cb62d5f94279c1336fef30beeb594584eaaf229304c48b020a44ec6f64ce36e28d85c7084bbcbc56b068d044f9248701685873e0f0c12f1";
		String a = RSAEncrypt.decryptParam(params);

		System.out.println(a);

		// RSAEncrypt rsaEncrypt= new RSAEncrypt();
		// //rsaEncrypt.genKeyPair();
		//
		// //加载公钥
		// try {
		// rsaEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY);
		// System.out.println("加载公钥成功:"+RSAEncrypt.DEFAULT_PUBLIC_KEY);
		// } catch (Exception e) {
		// System.err.println(e.getMessage());
		// System.err.println("加载公钥失败");
		// }
		//
		// //加载私钥
		// try {
		// rsaEncrypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY);
		// System.out.println("加载私钥成功:"+RSAEncrypt.DEFAULT_PRIVATE_KEY);
		// } catch (Exception e) {
		// System.err.println(e.getMessage());
		// System.err.println("加载私钥失败");
		// }
		//
		// //测试字符串
		// String encryptStr= "18502088330";
		// try {
		// //加密
		// String cipherStr =
		// "5a0a87786e1eac5fb382ad8043476affd95da07eed2dc08b250fdd8da6cdb4940c6714b96a62da2d473eba5ffda49392a2ff7b5fdb9e1474e637acc667a33bde63c70bb2726a5d8ef9168ea7204851481571ad6c1ffbe32297ef327630095cf051f1ba91ed1e8d89b5179414844a35d75479b6805b54393443406cf3c1f65d52";
		// // System.out.println(cipherStr.length());
		// //byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(),
		// encryptStr.getBytes());
		// byte[] cipher = hexStringToByteArray(cipherStr);
		// //解密
		// byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(),
		// cipher);
		// System.out.println("密文长度:"+ cipher.length);
		// System.out.println(RSAEncrypt.byteArrayToString(cipher));
		// System.out.println("明文长度:"+ plainText.length);
		// //System.out.println(RSAEncrypt.byteArrayToString(plainText));
		// System.out.println(new String(plainText));
		// } catch (Exception e) {
		// System.err.println(e.getMessage());
		// }

		// FileInputStream a = new FileInputStream("c:/pkcs8_private.pem");
		// RSAEncrypt.loadPrivateKey(a);
		// FileInputStream a = new FileInputStream("c:/rsa_public_key.pem");
		// RSAEncrypt.loadPublicKey(a);

	}
}
