package com.atguigu.gmall.order.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.cart.CartVo;
import com.atguigu.gmall.cart.SkuItem;
import com.atguigu.gmall.constant.CartConstant;
import com.atguigu.gmall.order.*;
import com.atguigu.gmall.order.mapper.OrderDetailMapper;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.atguigu.gmall.order.mapper.UserAddressMapper;
import com.atguigu.gmall.user.UserAddress;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    JedisPool jedisPool;

    @Reference
    CartService cartService;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Autowired
    UserAddressMapper userAddressMapper;

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

    @Transactional
    @Override
    public OrderInfo createOrder(Integer userId,OrderInfoTo orderInfoTo) {
        //1、找到购物车中所有需要下单的商品
        List<CartItem> cartItems = cartService.getCartInfoCheckedList(userId);
        CartVo cartVo = new CartVo();
        cartVo.setCartItems(cartItems);
        BigDecimal totalPrice = cartVo.getTotalPrice();//计算总价


        //OrderInfo，插入订单的信息
        OrderInfo orderInfo = new OrderInfo();  //总订单
        orderInfo.setOrderStatus(OrderStatus.UNPAID);
        orderInfo.setProcessStatus(ProcessStatus.UNPAID);
        orderInfo.setPaymentWay(PaymentWay.ONLINE);
        orderInfo.setCreateTime(new Date());
        //30分钟不用过期 1000*60*60
        long l = System.currentTimeMillis()+1000*60*30;
        orderInfo.setExpireTime(new Date(l));
        orderInfo.setUserId(userId);
        //设置订单收货人等信息
        orderInfo.setConsignee(orderInfoTo.getConsignee());
        orderInfo.setConsigneeTel(orderInfoTo.getConsigneeTel());
        orderInfo.setDeliveryAddress(orderInfoTo.getDeliveryAddress());
        orderInfo.setOrderComment(orderInfoTo.getOrderComment());
        //对外业务号
        orderInfo.setOutTradeNo("ATGUIGU_"+ System.currentTimeMillis()+"_"+userId);
        orderInfo.setTotalAmount(totalPrice);

        //加上订单描述;默认是第一个商品的名字
        orderInfo.setTradeBody(cartItems.get(0).getSkuItem().getSkuName());

        orderInfoMapper.insert(orderInfo);


        List<OrderDetail> orderDetailList = new ArrayList<>();
        //2、这些商品对应的是OrderDetail；
        for (CartItem cartItem : cartItems) {
            SkuItem skuItem = cartItem.getSkuItem();
            OrderDetail orderDetail = new OrderDetail(); //订单项
            orderDetail.setImgUrl(skuItem.getSkuDefaultImg());
            orderDetail.setOrderId(orderInfo.getId());
            orderDetail.setOrderPrice(skuItem.getPrice());
            orderDetail.setSkuId(skuItem.getId());
            orderDetail.setSkuName(skuItem.getSkuName());
            orderDetail.setSkuNum(cartItem.getNum());
            orderDetailMapper.insert(orderDetail);
            orderDetailList.add(orderDetail);
        }

        orderInfo.setOrderDetailList(orderDetailList);
        
        //3、所有的OrderDetail才组成一个OrderInfo
        //以上完成了，删掉购物车以上东西
        Jedis jedis = jedisPool.getResource();
        String[] delStrIds = new String[cartItems.size()];
        for (int i=0;i<cartItems.size();i++){
            delStrIds[i] = cartItems.get(i).getSkuItem().getId()+"";
        }
        //删购物车数据
        jedis.hdel(CartConstant.USER_CART_PREFIX+userId,delStrIds);


        //redis中购物车原来列表顺序
        String fieldOrder = jedis.hget(CartConstant.USER_CART_PREFIX + userId, "fieldOrder");
        List list = JSON.parseObject(fieldOrder, List.class);


        //redis中购物车新列表顺序
        List<Integer> newfieldOrder = new ArrayList<>();
        //遍历原来列表的顺序，只要不是删除项都可以添加在新列表中
        //1,2,3   2,3
        for (Object o : list) {
            Integer id = Integer.parseInt(o.toString());
            boolean exist = false;
            for (CartItem cartItem : cartItems) {
                if(cartItem.getSkuItem().getId() == id){
                    //原列表中的id项在删除项中有
                    exist = true;
                }
            }

            if(!exist){
                //如果没有就添加到新列表顺序中
                newfieldOrder.add(id);
            }

        }

        //更新列表顺序
        jedis.hset(CartConstant.USER_CART_PREFIX+userId,"fieldOrder",JSON.toJSONString(newfieldOrder));
        jedis.close();

        //返回刚才创建好保存到数据库的订单；
        return orderInfo;

    }

    @Override
    public UserAddress getUserAddressById(Integer userAddressId) {

        return  userAddressMapper.selectById(userAddressId);
    }

    @Override
    public OrderInfo getOrderById(Integer id) {
        return orderInfoMapper.selectById(id);
    }

    /**
     * 将订单状态改为支付成功
     * @param out_trade_no
     */
    @Override
    public void updateOrderPaySuccess(String out_trade_no) {
        OrderInfo where = new OrderInfo();
        where.setOutTradeNo(out_trade_no);

        OrderInfo orderInfo = new OrderInfo();
        //为null的不会修改
        orderInfo.setProcessStatus(ProcessStatus.PAID);
        orderInfo.setOrderStatus(OrderStatus.PAID);

        //new UpdateWrapper<OrderInfo>(where) 用来生产where的 where out_trade_no=xxxx
        orderInfoMapper.update(orderInfo,new UpdateWrapper<OrderInfo>(where));
    }

    /**
     * 第三方的所有功能怎么掉？
     *  发短信？
     *
     * @param skuId
     * @param num
     * @return
     * @throws IOException
     */
    private boolean stockCheck(Integer skuId,Integer num) throws IOException {
        //1、验证用户购物车里面勾选的商品的每一个库存是否足够
        //1）、HttpClient
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //2）、
        HttpGet httpGet = new HttpGet("http://www.gware.com/hasStock?skuId="+skuId+"&num="+num);
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
