package com.atguigu.gmall.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单提交需要的数据
 */
@Data
public class OrderSubmitVo implements Serializable{

    private Integer userAddressId;//用户地址的id
    private String orderComment;//订单的备注

    private String tradeToken;//防重复提交令牌

}
