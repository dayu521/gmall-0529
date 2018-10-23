package com.atguigu.gmall.order.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.order.OrderService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    JedisPool jedisPool;

    @Reference
    CartService cartService;


    @Override
    public String createTradeToken() {
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        Jedis jedis = jedisPool.getResource();
        //服务端设置了令牌
        jedis.setex(token,60*3,"66666");

        return token;
    }

    //验证令牌
    @Override
    public boolean verfyToken(String token) {
        Jedis jedis = jedisPool.getResource();
        Long del = jedis.del(token);
        return del==1L?true:false;
    }

    @Override
    public List<String> verfyStock(Integer userId) throws IOException {


        List<CartItem> cartItems = cartService.getCartInfoCheckedList(userId);

        List<String> result = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            //每一个库存
            boolean b = stockCheck(cartItem.getSkuItem().getId(), cartItem.getNum());
            if(!b){
                //验证失败
                result.add(cartItem.getSkuItem().getSkuName());
            }
        }

        return result;
    }

    private boolean stockCheck(Integer skuId,Integer num) throws IOException {
        //1、验证用户购物车里面勾选的商品的每一个库存是否足够
        //1）、HttpClient
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //2）、
        HttpGet httpGet = new HttpGet("http://http://www.gware.com/hasStock?skuId="+skuId+"&num="+num);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            // 404 NOTFOUD
            //获取响应体
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent(); //0或者1
            String data = EntityUtils.toString(entity);
            return  "0".equals(data)?false:true;

        }  finally {
            //关响应
            response.close();
        }
    }
}
