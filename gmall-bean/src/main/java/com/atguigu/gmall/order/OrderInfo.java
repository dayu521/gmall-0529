package com.atguigu.gmall.order;

import com.atguigu.gmall.SuperBean;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderInfo extends SuperBean {


    private String consignee; //收货人

    private String consigneeTel; //电话

    private String deliveryAddress; //收货地址

    private BigDecimal totalAmount; //订单总额

    private OrderStatus orderStatus;  //订单状态

    private ProcessStatus processStatus; //订单进度状态

    private Integer userId;  //用户id

    private PaymentWay paymentWay;//支付方式

    private Date expireTime;  //过期时间

    private String tradeBody;//订单描述

    private String orderComment; //订单备注

    private Date createTime; //订单创建时间

    private String parentOrderId; //父订单id

    private String trackingNo; //物流单号

    private String wareId;

    private String outTradeNo;  //对外交易号；对接支付宝

    @TableField(exist = false)
    private List<OrderDetail> orderDetailList;//所有订单项的集合


    @TableField(exist = false)
    private List<OrderInfo> orderSubList; //子订单



}
