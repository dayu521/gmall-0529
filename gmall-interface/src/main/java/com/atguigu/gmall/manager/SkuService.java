package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.es.SkuBaseAttrEsVo;
import com.atguigu.gmall.manager.sku.SkuAttrValueMappingTo;
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

    /**
     * 查询某个sku的详细信息
     * @param skuId
     * @return
     */
    SkuInfo getSkuInfoBySkuId(Integer skuId) throws InterruptedException;

    /**
     *  查出当前sku对应的spu下面所有sku销售属性值的组合
     * @param spuId  插入spuId
     * @return
     */
    List<SkuAttrValueMappingTo> getSkuAttrValueMapping(Integer spuId);


    /**
     * 获取sku所有平台属性值的id集合
     * @param skuId
     * @return
     */
    List<SkuBaseAttrEsVo> getSkuBaseAttrValueIds(Integer skuId);

    /**
     * 根据属性值的集合找出这个属性值所在的平台属性下的所有属性值
     * @param valueIds
     * @return
     */
    List<BaseAttrInfo> getBaseAttrInfoByAttrValueIdIn(List<Integer> valueIds);
}
