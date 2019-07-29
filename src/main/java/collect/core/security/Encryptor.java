package collect.core.security;
 
import java.io.UnsupportedEncodingException; 
import org.bouncycastle.util.encoders.Hex; 
import collect.core.security.SM3Digest;

/**
 * MD5摘要算法
 * 
 * @author hedy
 * 
 */
public class Encryptor { 

	public static String SM3(String content){
		byte[] md = new byte[32];
		byte[] msg1 = null;
		try {
			msg1 = content.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SM3Digest sm3 = new SM3Digest();
		sm3.update(msg1, 0, msg1.length);
		sm3.doFinal(md, 0);
		String s = new String(Hex.encode(md));
		//System.out.println(s);
		//System.out.println(s.length());

		return s;
	} 

}
