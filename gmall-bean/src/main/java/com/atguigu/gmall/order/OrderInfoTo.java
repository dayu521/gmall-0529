package com.atguigu.gmall.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderInfoTo implements Serializable {
    private String orderComment;//订单的备注

    private String consignee; //收货人

    private String consigneeTel; //电话

    private String deliveryAddress; //收货地址


}
