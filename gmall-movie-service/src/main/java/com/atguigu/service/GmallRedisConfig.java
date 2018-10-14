package com.atguigu.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.UnknownHostException;

@Configuration
public class GmallRedisConfig {

    //自定义RedisTemplate策略;方便使用RedisTemplate操作对象
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        //设置默认的序列化策略
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        //设置key的序列化策略
        template.setKeySerializer(new StringRedisSerializer());
        //设置值的序列化策略
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    //暴露JedisPool;方便获取原生的jedis客户端
    //JedisConnectionFactory是redis自动配置已经做好的。我们直接注入使用
    @Bean
    public JedisPool jedisPoolConfig(JedisConnectionFactory factory) {
        JedisPoolConfig config = factory.getPoolConfig();
        JedisPool jedisPool = new JedisPool(config, factory.getHostName(), factory.getPort(), factory.getTimeout());
        return jedisPool;
    }
}
