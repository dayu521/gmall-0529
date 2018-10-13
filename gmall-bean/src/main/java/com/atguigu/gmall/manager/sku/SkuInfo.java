package com.atguigu.gmall.manager.sku;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Sku信息表
 */
@Data
public class SkuInfo extends SuperBean {

    //spu_id  price   sku_name  weight   tm_id  catalog3_id  sku_default_img
    private Integer spuId;//当前sku对应的spuId
    private BigDecimal price;//当前价格
    private String skuName;//sku名字
    private String skuDesc;//sku描述
    private BigDecimal  weight;//重量
    private Integer tmId;//品牌id
    private Integer catalog3Id;//三级分类id(冗余)
    private String skuDefaultImg;//sku默认图片路径（冗余）
}
