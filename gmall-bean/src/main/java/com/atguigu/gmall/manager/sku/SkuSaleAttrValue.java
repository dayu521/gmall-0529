package com.atguigu.gmall.manager.sku;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * sku销售属性值表
 */
@Data
public class SkuSaleAttrValue extends SuperBean {

    //sku_id  sale_attr_id  sale_attr_value_id  sale_attr_name  sale_attr_value_name
    private Integer skuId;//
    private Integer saleAttrId;//销售属性的id
    private String saleAttrName;//销售属性的名字（冗余） ===【颜色】

    private Integer saleAttrValueId;//销售属性值id
    private Integer saleAttrValueName;//销售属性值的名字  ====【红色】


}
