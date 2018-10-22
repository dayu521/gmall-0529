package com.atguigu.gmall.cart;

import com.atguigu.gmall.manager.sku.SkuInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车的每一项数据
 */
@Setter
public class CartItem implements Serializable {

    @Getter
    private SkuItem skuItem; //当前这一个购物项商品的详情
    @Getter
    private Integer num;//当前项数量
    private BigDecimal totalPrice;//当前项的总价

    /**
     * 自动算这一项的总价
     * @return
     */
    public BigDecimal getTotalPrice(){
        BigDecimal multiply = skuItem.getPrice().multiply(new BigDecimal(num));
        this.totalPrice = multiply;
        return  multiply;
    }



}
