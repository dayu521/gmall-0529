package com.atguigu.service;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.user.Movie;
import com.atguigu.gmall.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Autowired
    RedisProperties redisProperties;

    @Autowired
    JedisPool jedisPool;
    @Test
    public void test02(){

        //System.out.println(factory);
        Jedis jedis = jedisPool.getResource();
        //获取成功返回的就是ok
        String set = jedis.set("hello", UUID.randomUUID().toString(), "NX", "EX", 60);
        System.out.println("获取到分布式锁..."+set);
    }

    @Test
    public void test01(){

//        redisTemplate.opsForValue().set("abcd",new Movie("1","西游记",new User("1","s")));
//        System.out.println("操作完成。。。");


        Jedis jedis = jedisPool.getResource();
        String set = jedis.set("lock.key", UUID.randomUUID().toString(), "NX", "EX", 60);
        System.out.println("获取到分布式锁..."+set);

        //stringRedisTemplate.opsForValue().set("abc","def");
//        Movie abcd = (Movie) redisTemplate.opsForValue().get("abcd");
//        System.out.println(abcd.getUser().getUserName());
//        redisTemplate.opsForValue().set("skuInfoKey",abcd,1, TimeUnit.SECONDS);
//
//        redisTemplate.execute(new RedisCallback() {
//
//            @Override
//            public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                //connection.
//                Jedis jedis = new Jedis();
//                //jedis.set()
//                //返回不了？？？？
//                connection.set("lock.key".getBytes(),"lock.value".getBytes(), Expiration.seconds(60), RedisStringCommands.SetOption.SET_IF_ABSENT);
//
//                //connection.
//                System.out.println("加锁完成......");
//                return null;
//            }
//        });



    }
}
