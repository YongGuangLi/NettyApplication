package com.application;
 

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class CustomRedisSerializer<T> implements RedisSerializer<T> { 
    @Override
    public byte[] serialize(T t) throws SerializationException {
  
        return (byte[]) t;
    }

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		// TODO Auto-generated method stub
		return null;
	}
 

}
