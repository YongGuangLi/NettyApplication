package collect.core.security;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import io.netty.util.ByteProcessor;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

//import org.apache.commons.codec.binary.Base64;

/**
 * 非对称加密算法RSA 
 * 非对称加密的效率较低，故非对称加密算法常用来加密少量数据  如:对称加密算法的秘钥， 真正数据的加密使用 对称加密算法
 * 而公钥和私钥的服务器端或客户端保存的时候一般使用Base64编码或者Hex编码，Hex编码的长度要小于Base64(推荐Hex保存) 
 * 
 */
public class RSACoder {

	//秘钥算法 -->秘钥生成的算法
	private static final String KEY_ALGORITHM = "RSA";

	private static final int KEY_SIZE = 1024; //RSA密钥长度，默认1024位，范围512~65536之间，必须是64的倍数

	public static String getPublicKeyString() {
		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2Twm5syehm42VLNc/P7eLrSHC8+Nc5UZtraGTQF3g0qNCZntMFK4eyi3+3PenaUDx6Yuosxl/Ar7sYt866ZyrijV+9Qn5ocxH7fNAtCJo6G2nIgV8k45q4Dkrc93rM8keuTYL+8egoQMn5r61IJcVm/8tqSvvqA2QdINs3OoIkwIDAQAB";	
	}
	public static String getPrivateKeyString() {
		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		return  "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALZPCbmzJ6GbjZUs1z8/t4utIcLz41zlRm2toZNAXeDSo0Jme0wUrh7KLf7c96dpQPHpi6izGX8Cvuxi3zrpnKuKNX71CfmhzEft80C0ImjobaciBXyTjmrgOStz3eszyR65Ngv7x6ChAyfmvrUglxWb/y2pK++oDZB0g2zc6giTAgMBAAECgYB+7HoN4k/2MHEa6XQWIcssoGW/78JcO3eUBm1WMlrpmlx1bxnqNv+eig4JwVdeI2ZGxd0on0Bh78xykMdvoZMjn1SdGGdojqQqvl/9JWbs5COnp6xCmHUUYChqwSwi1DESaU7vy7x/DDL014jthkUuIALvi9ICSiopKZetKjJzoQJBAPTJAKDCsYFMVwbph1RL6wbKfolDqBHqaIRFzhn/Vwc+cGxCWl2lH6MpxfhpfwaRFekvrGpyDU8+VpaDDnAobUMCQQC+qUWds/kCkAN4jabIrHLyHOmGXC9W/tYcnMq2Ng+f6lWjUkAmA21mlCVnzOvSABgZMFwGYVXUzE/vfa4MExpxAkBQud0QJ0T8vB1TNT6hCSxoH+2c/Qf3aJOxU4gFwNSFmgzfVv6QNX16hLM6Gih/FOfh8AvvSs+i248Ysf3X9S0bAkB9ZtZsCHCSl+hAhWKagJpF1nBeOAnG/WwUe6f0upACTV2wQywAFKcDHhOQNz8v63HuN1l/tMuAYDV/PrmYJYRxAkEAoC5vc8sa8EsAk6Yi9QEi3mJESZWFplgiBHVPK0KQ7h9Mw7pidzYJn8u19F2fkyYow5u/asNXpzhAnPVcXpHPaQ==";
	}

	//	public static void main(String[] args) {
	//		
	//		String str = "hello world";
	//		
	//		//初始化密钥 
	//		KeyPair keyPair = initKey();
	//
	//		final PublicKey publicKey = keyPair.getPublic();
	//		final PrivateKey privateKey = keyPair.getPrivate();
	//		
	//		byte [] publicByte = Base64.decodeBase64(publicKeyString);
	//		byte [] privateByte = Base64.decodeBase64(privateKeyString);
	//		
	//		System.out.println("公钥:"+Base64.encodeBase64String(publicKey.getEncoded()));
	//		System.out.println("私钥:"+Base64.encodeBase64String(privateKey.getEncoded()));
	//		
	//		// =============> 用字符串保存 ，公钥加密，私钥解密<=============
	//		//加密 
	//		byte[] encryptDa = handleByPublic(str.getBytes(), publicByte, true);
	//		//解密 
	//		byte[] decryptDa = handleByPrivate(encryptDa, privateByte, false);
	//		
	//		System.out.println(new String(decryptDa));
	//		
	//		// =============>公钥加密，私钥解密<=============
	//		//加密 
	//		byte[] encryptData = handleByPublic(str.getBytes(), publicKey.getEncoded(), true);
	//		//解密 
	//		byte[] decryptData = handleByPrivate(encryptData, privateKey.getEncoded(), false);
	//		
	//		System.out.println(new String(decryptData));
	//		
	//		
	//		// =============>私钥加密，公钥解密<=============
	//
	//		//加密
	//		byte[] encryptDat = handleByPrivate(str.getBytes(), privateKey.getEncoded(), true);
	//		//解密
	//		byte[] decryptDat = handleByPublic(encryptDat, publicKey.getEncoded(), false);
	//		
	//		System.out.println(new String(decryptDat));
	//		
	//	}


	public static KeyPair initKey(){
		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		try {
			//实例化密钥对 生成器
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);

			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			return keyPair;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}



	//使用公钥加密或解密 
	public static byte[] handleByPublic(byte[] data,byte[]key,boolean isEncrypt){
		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			//生成公钥 
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			//对数据加密或解密 
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			if(isEncrypt){
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);				
			}else{
				cipher.init(Cipher.DECRYPT_MODE, publicKey);
			}

			return cipher.doFinal(data);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


	}

	//使用私钥加密或解密 
	public static byte[] handleByPrivate(byte[] data,byte[]key,boolean isEncrypt){
		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			//生成私钥
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			//对数据加密或解密 
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			if(isEncrypt){
				cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			}else{
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
			}

			return cipher.doFinal(data);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	} 
	
	public static String getEncodePasswd(String passwd) {
		String encodePasswd = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] bash = null;
			try {
				bash = messageDigest.digest(passwd.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			encodePasswd = Hex.encodeHexString(bash);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encodePasswd;
	}
}