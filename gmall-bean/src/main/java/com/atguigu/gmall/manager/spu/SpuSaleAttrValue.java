package com.atguigu.gmall.manager.spu;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * Spu销售属性的值
 */
@Data
public class SpuSaleAttrValue extends SuperBean {

    //spu_id  sale_attr_id  sale_attr_value_name
    private Integer spuId;
    private Integer saleAttrId;
    private String  saleAttrValueName;
}
