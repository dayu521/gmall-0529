package com.atguigu.gmall.manager.es;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Es中保存的skuInfo的信息
 */
@Data
public class SkuInfoEsVo implements Serializable {

    Integer id;//sku的id

    BigDecimal price; //sku的价格

    String skuName; //sku的名字   //全文检索，分词

    String skuDesc; //sku的描述

    Integer catalog3Id; //sku的3级分类id

    String skuDefaultImg; //sku的默认图片地址

    Long hotScore=0L; //热度评分


    List<SkuBaseAttrEsVo> baseAttrEsVos; //把sku的平台属性的值保存过来，只需要存平台属性值的id;


}
