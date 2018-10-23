package com.atguigu.gmall.order;

import com.atguigu.gmall.user.UserAddress;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    /**
     * 创建当次的一个交易令牌
     * @return
     */
    String createTradeToken();

    /**
     * 验证令牌，防重复提交
     * @param token
     * @return
     */
    boolean verfyToken(String token);

    /**
     * 验证库存
     * @param userId 用户id
     * @return 所有库存不足的商品信息
     */
    List<String> verfyStock(Integer userId) throws IOException;


    /**
     *
     * @param userId     用户的id
     * @param submitVo  订单的备注、收货人等信息
     */
    void createOrder(Integer userId,OrderInfoTo submitVo);

    /**
     * 获取用户的地址
     * @param userAddressId
     * @return
     */
    UserAddress getUserAddressById(Integer userAddressId);
}
