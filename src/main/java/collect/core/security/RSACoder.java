package collect.core.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

//import org.apache.commons.codec.binary.Base64;

/**
 * 非对称加密算法RSA 非对称加密的效率较低，故非对称加密算法常用来加密少量数据 如:对称加密算法的秘钥， 真正数据的加密使用 对称加密算法
 * 而公钥和私钥的服务器端或客户端保存的时候一般使用Base64编码或者Hex编码，Hex编码的长度要小于Base64(推荐Hex保存)
 * 
 */
public class RSACoder {

	// 秘钥算法 -->秘钥生成的算法
	private static final String KEY_ALGORITHM = "RSA";

	private static final int KEY_SIZE = 1024; // RSA密钥长度，默认1024位，范围512~65536之间，必须是64的倍数

	public static String getPublicKeyString() {

		return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2Twm5syehm42VLNc/P7eLrSHC8+Nc5UZtraGTQF3g0qNCZntMFK4eyi3+3PenaUDx6Yuosxl/Ar7sYt866ZyrijV+9Qn5ocxH7fNAtCJo6G2nIgV8k45q4Dkrc93rM8keuTYL+8egoQMn5r61IJcVm/8tqSvvqA2QdINs3OoIkwIDAQAB";
	}
	// public static void main(String[] args) {
	//
	// String str = "hello world";
	//
	// //初始化密钥
	// KeyPair keyPair = initKey();
	//
	// final PublicKey publicKey = keyPair.getPublic();
	// final PrivateKey privateKey = keyPair.getPrivate();
	//
	// byte [] publicByte = Base64.decodeBase64(publicKeyString);
	// byte [] privateByte = Base64.decodeBase64(privateKeyString);
	//
	// System.out.println("公钥:"+Base64.encodeBase64String(publicKey.getEncoded()));
	// System.out.println("私钥:"+Base64.encodeBase64String(privateKey.getEncoded()));
	//
	// // =============> 用字符串保存 ，公钥加密，私钥解密<=============
	// //加密
	// byte[] encryptDa = handleByPublic(str.getBytes(), publicByte, true);
	// //解密
	// byte[] decryptDa = handleByPrivate(encryptDa, privateByte, false);
	//
	// System.out.println(new String(decryptDa));
	//
	// // =============>公钥加密，私钥解密<=============
	// //加密
	// byte[] encryptData = handleByPublic(str.getBytes(), publicKey.getEncoded(),
	// true);
	// //解密
	// byte[] decryptData = handleByPrivate(encryptData, privateKey.getEncoded(),
	// false);
	//
	// System.out.println(new String(decryptData));
	//
	//
	// // =============>私钥加密，公钥解密<=============
	//
	// //加密
	// byte[] encryptDat = handleByPrivate(str.getBytes(), privateKey.getEncoded(),
	// true);
	// //解密
	// byte[] decryptDat = handleByPublic(encryptDat, publicKey.getEncoded(),
	// false);
	//
	// System.out.println(new String(decryptDat));
	//
	// }

	public static KeyPair initKey() {
		try {
			// 实例化密钥对 生成器
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);

			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			return keyPair;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// 使用公钥加密或解密
	public static byte[] handleByPublic(byte[] data, byte[] key, boolean isEncrypt) {
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			// 生成公钥
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			// 对数据加密或解密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			if (isEncrypt) {
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, publicKey);
			}

			return cipher.doFinal(data);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// 使用私钥加密或解密
	public static byte[] handleByPrivate(byte[] data, byte[] key, boolean isEncrypt) {
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			// 生成私钥
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			// 对数据加密或解密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			if (isEncrypt) {
				cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
			}

			return cipher.doFinal(data);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
