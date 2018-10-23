package com.atguigu.gmall.order;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetail extends SuperBean {

    private Integer orderId; //订单的id
    private Integer skuId; //商品id
    private String skuName;//商品名
    private String imgUrl;//商品图片
    private BigDecimal orderPrice;//商品的单价
    private Integer skuNum; //商品的数量




}
