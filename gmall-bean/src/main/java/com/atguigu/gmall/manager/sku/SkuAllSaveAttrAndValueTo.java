package com.atguigu.gmall.manager.sku;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 传输数据；实现序列化接口
 */
@Data
public class SkuAllSaveAttrAndValueTo implements Serializable {

    //  id  spu_id  sale_attr_id  sale_attr_name  sale_attr_value_id  sale_attr_value_name
    //   sku_id  is_check
    private Integer id;
    private Integer spuId;
    private Integer saleAttrId;
    private String saleAttrName;
    // sale_attr_value_id  sale_attr_value_name   sku_id  is_check
    private List<SkuAllSaveAttrValueContentTo> valueContent;



}
