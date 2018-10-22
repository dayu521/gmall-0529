package com.atguigu.gmall.cart;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartVo implements Serializable{

    List<CartItem> cartItems;
    private BigDecimal totalPrice;

}
