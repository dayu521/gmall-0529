package com.atguigu.gmall.manager.spu;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * Spu销售属性
 */
@Data
public class SpuSaleAttr extends SuperBean {

    //spu_id  sale_attr_id  sale_attr_name
    private Integer spuId;
    private Integer saleAttrId;
    private String saleAttrName;
}
