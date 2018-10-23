package com.atguigu.gmall.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class GmallOrderConfig {

    @Bean
    public JedisPool jedisPoolConfig(JedisConnectionFactory factory) {
        //1、连接工厂中所有信息都有。
        JedisPoolConfig config = factory.getPoolConfig();

        JedisPool jedisPool = new JedisPool(config, factory.getHostName(), factory.getPort(), factory.getTimeout());
        return jedisPool;
    }
}
