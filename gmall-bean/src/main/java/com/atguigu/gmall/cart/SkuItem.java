package com.atguigu.gmall.cart;

import com.atguigu.gmall.manager.sku.SkuSaleAttrValue;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车商品项数据
 */
@Data
public class SkuItem implements Serializable {

    //spu_id  price   sku_name  weight   tm_id  catalog3_id  sku_default_img
    private Integer id;
    private Integer spuId;//当前sku对应的spuId
    private BigDecimal price;//当前价格
    private String skuName;//sku名字
    private String skuDesc;//sku描述
    private BigDecimal  weight;//重量

    private Integer tmId;//品牌id
    private Integer catalog3Id;//三级分类id(冗余)
    private String skuDefaultImg;//sku默认图片路径（冗余）
    private List<SkuSaleAttrValue> skuSaleAttrValues;//sku所有销售属性的值
}
