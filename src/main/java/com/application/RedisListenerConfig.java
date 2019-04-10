package com.application;
  
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory; 
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter; 
import org.springframework.stereotype.Component;

import up.client.netty.NettyClient;

@ComponentScan("up.client.netty")
@Component
public class RedisListenerConfig { 
 
	@Autowired
	private NettyClient nettyClient;  
	
	@Value("${server.address}")
	private String ServerAddress;
	
	@Value("${server.port}")
	private int ServerPort;
	
	@Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter) {
 
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("NettyClient"));   //订阅topic   
		
        return container;
    }
 
	@Bean
    MessageListenerAdapter listenerAdapter() {
		RedisMessageListener redisMessageListener = new RedisMessageListener(nettyClient, ServerAddress, ServerPort);   
        return new MessageListenerAdapter(redisMessageListener);
    }
	
}
