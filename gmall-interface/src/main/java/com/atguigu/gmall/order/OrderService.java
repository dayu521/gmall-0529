package com.atguigu.gmall.order;

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
}
