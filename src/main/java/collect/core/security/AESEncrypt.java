package collect.core.security;

import java.io.File;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESEncrypt {

	/**
	 * @return password
	 */
	public static String getKey() {
		return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDrDxPpGO++cNUPfD0BvN2/d7I9Ge3UgLmg10TkTL7mJJkC3g6OrUIpi7bZOcYFbMwHoMiscavEDH2JLiqf/rBvbpbEf11JnT8/6UADcv/no94tq0w3KffFg5ViEJhVwfnlcSSO6WKEDxOk0JZT6zSrE02hj+k3y+Ye19kiN10twQIDAQAB";
	}

 

	/**
	 * AES加密字符串
	 *
	 * @param content
	 *            需要被加密的字符串
	 * @param
	 *            加密需要的密码
	 * @return 密文
	 */
	public static String encrypt(String content) throws Exception {

		return byte2hex(encrypt(content, getKey()));

	}

	/**
	 * AES加密字符串
	 *
	 * @param content
	 *            需要被加密的字符串
	 * @param password
	 *            加密需要的密码
	 * @return 密文
	 */
	public static byte[] encrypt(String content, String password) throws Exception {

		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(password.getBytes());
		kgen.init(128, random);
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");// 创建密码器
		byte[] byteContent = content.getBytes("utf-8");
		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(byteContent);
		return result; // 加密

	}

	 
	/**
	 * 解密AES加密过的字符串
	 * 
	 * @param content
	 *            AES加密过过的内容
	 * @param
	 *            加密时的密码
	 * @return 明文
	 * @throws Exception 
	 */
	public static String decrypt(String content) throws Exception {
		byte[] decrypt = decrypt(hex2byte(content), getKey());

		return new String(decrypt,"utf-8");
	}

	/**
	 * 解密AES加密过的字符串
	 * 
	 * @param content
	 *            AES加密过过的内容
	 * @param password
	 *            加密时的密码
	 * @return 明文
	 * @throws Exception 
	 */
	public static byte[] decrypt(byte[] content, String password) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");  
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(password.getBytes());
		kgen.init(128, random);
		//kgen.init(128, new SecureRandom(password.getBytes()));  
		SecretKey secretKey = kgen.generateKey();  
		byte[] enCodeFormat = secretKey.getEncoded();  
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
		Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
		cipher.init(Cipher.DECRYPT_MODE, key);// 初始化  
		byte[] result = cipher.doFinal(content);  
		return result;   
	}

	/**
	 * byte数组转换成十六进制字符串
	 */
	public static String byte2hex(byte[] buf) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {  
			String hex = Integer.toHexString(buf[i] & 0xFF);  
			if (hex.length() == 1) {  
				hex = '0' + hex;  
			}  
			sb.append(hex.toUpperCase());  
		}  
		return sb.toString();  
	}

	/**
	 * 十六进制字符串转换成byte数组
	 */
	public static byte[] hex2byte(String hexStr) throws Exception {
		if (hexStr.length() < 1)
			return null;  
		byte[] result = new byte[hexStr.length()/2];  
		for (int i = 0;i< hexStr.length()/2; i++) {  
			int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
			int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
			result[i] = (byte) (high * 16 + low);  
		}  
		return result;  
	} 
}
