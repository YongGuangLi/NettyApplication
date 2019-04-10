package com.application;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; 
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
 
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NettyApplication { 
	
	public static void main(String[] args) {
		SpringApplication.run(NettyApplication.class, args);
		
		synchronized (NettyApplication.class) {
			while (true) {
				try {
					NettyApplication.class.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}  
}
