package com.atguigu.gmall.order;

import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.user.UserAddress;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TradePageVo implements Serializable{


    private List<UserAddress> userAddresses;//用户收货列表
    private List<CartItem> cartItems;//商品清单

    private BigDecimal totalPrice; //商品总价




}
