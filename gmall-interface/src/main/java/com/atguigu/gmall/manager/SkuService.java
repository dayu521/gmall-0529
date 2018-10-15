package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.sku.SkuInfo;
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

    /**
     * 大保存sku
     * @param skuInfo
     */
    void saveBigSkuInfo(SkuInfo skuInfo);

    /**
     * 获取spu对应的skuInfo
     * @param spuId
     * @return
     */
    List<SkuInfo> getSkuInfoBySpuId(Integer spuId);
}
