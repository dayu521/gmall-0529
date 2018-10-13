package com.atguigu.gmall.manager.spu;

import com.atguigu.gmall.SuperBean;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * Spu销售属性
 *
 *  {id:1,spuId:1,saleAttrId:1,saleAttrName:'颜色',saleAttrValues:[
 *      {id:1,name:'红色'},
 *      {id:2,name:'黑色'}
 *  ]}
 */
@Data
public class SpuSaleAttr extends SuperBean {

    //spu_id  sale_attr_id  sale_attr_name
    private Integer spuId;
    private Integer saleAttrId;
    private String saleAttrName;

    @TableField(exist = false)
    private List<SpuSaleAttrValue> saleAttrValues;
}
