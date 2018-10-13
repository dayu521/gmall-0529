package com.atguigu.gmall.manager.sku;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * Sku与平台属性关联信息
 */
@Data
public class SkuAttrValue extends SuperBean{

    //attr_id  value_id  sku_id
    private Integer attrId;//平台属性id
    private Integer valueId;//平台属性值id
    private Integer skuId;//对应的skuId

}
