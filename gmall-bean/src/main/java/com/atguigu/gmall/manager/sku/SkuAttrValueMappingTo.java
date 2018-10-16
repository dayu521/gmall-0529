package com.atguigu.gmall.manager.sku;

import lombok.Data;

import java.io.Serializable;

/**
 * 保存销售属性值和skuid的映射关系
 */
@Data
public class SkuAttrValueMappingTo implements Serializable {

    //sku_id  spu_id  sku_name                  sale_attr_value_id  sale_attr_value_name
    private Integer skuId;//sku的id
    private Integer spuId;
    private String skuName;
    private String saleAttrValueIdMapping; //销售属性值的映射
    private String saleAttrValueNameMapping;//销售属性值名字的映射（提示）

}
