package com.atguigu.gmall.manager.sku;

import com.atguigu.gmall.SuperBean;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * Sku信息表
 */
@NoArgsConstructor
@AllArgsConstructor
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

    @TableField(exist = false)
    private List<SkuImage> skuImages;//sku的所有需要保存的图片

    @TableField(exist = false)
    private List<SkuAttrValue> skuAttrValues;//sku的所有平台属性的值

    @TableField(exist = false)
    private List<SkuSaleAttrValue> skuSaleAttrValues;//sku所有销售属性的值

    @TableField(exist = false)
    private List<SkuAllSaveAttrAndValueTo> skuAllSaveAttrAndValueTos;






}
