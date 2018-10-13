package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.spu.SpuSaleAttr;

import java.util.List;

/**
 * Sku服务组件
 */
public interface SkuService  {
    List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id);

    /**
     *按照spuId查询出这个spu对应的所有的销售属性的名和值
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrBySpuId(Integer spuId);
}
