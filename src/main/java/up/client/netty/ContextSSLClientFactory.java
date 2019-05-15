package up.client.netty;

/**
 * @ClassName: ContextSSLFactory
 * @Description: SSL认证
 * @Author: chenjd
 * @Date: 2019-04-25 17:19
 **/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource; 
/**
 * 初始化sslcontext类
 *
 */
public class ContextSSLClientFactory {

	private static final SSLContext SSL_CONTEXT_C ;

	static{
		SSLContext sslContext2 = null ;
		try {
			sslContext2 = SSLContext.getInstance("SSLv3") ;
		} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
		}
		try{
			if(getKeyManagersClient() != null && getTrustManagersClient() != null){
				sslContext2.init(getKeyManagersClient(), getTrustManagersClient(), null);
			} 
		}catch(Exception e){
			e.printStackTrace() ;
		}
		sslContext2.createSSLEngine().getSupportedCipherSuites() ;
		SSL_CONTEXT_C = sslContext2 ;
	}
	
	public ContextSSLClientFactory(){

	}

	public static SSLContext getSslContext2(){
		return SSL_CONTEXT_C ;
	}
	
	private static TrustManager[] getTrustManagersClient(){
		InputStream inputStream = null;
		KeyStore ks = null ;
		TrustManagerFactory keyFac = null ;

		TrustManager[] kms = null ;
		try { 
			ClassPathResource resource = new ClassPathResource("/cChat.jks");
			inputStream = resource.getInputStream();

			// 获得KeyManagerFactory对象. 初始化位默认算法
			keyFac = TrustManagerFactory.getInstance("SunX509") ;

			ks = KeyStore.getInstance("JKS") ;
			String keyStorePass = "sNetty" ;
			ks.load(inputStream , keyStorePass.toCharArray()) ;
			keyFac.init(ks) ;
			kms = keyFac.getTrustManagers() ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(inputStream != null ){
				try {
					inputStream.close() ;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return kms ;
	}
	
	private static KeyManager[] getKeyManagersClient(){
		InputStream inputStream = null;
		KeyStore ks = null ;
		KeyManagerFactory keyFac = null ;

		KeyManager[] kms = null ;
		try {
			ClassPathResource resource = new ClassPathResource("/cChat.jks");
			inputStream = resource.getInputStream();
			// 获得KeyManagerFactory对象. 初始化位默认算法
			keyFac = KeyManagerFactory.getInstance("SunX509") ; 
			ks = KeyStore.getInstance("JKS") ;
			String keyStorePass = "sNetty" ;
			ks.load(inputStream , keyStorePass.toCharArray()) ;
			keyFac.init(ks, keyStorePass.toCharArray()) ;
			kms = keyFac.getKeyManagers() ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(inputStream != null ){
				try {
					inputStream.close() ;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return kms ;
	}
}
