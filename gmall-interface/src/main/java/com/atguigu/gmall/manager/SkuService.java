package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.spu.SpuSaleAttr;

import java.util.List;

public interface SkuService {
    List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id);

    List<SpuSaleAttr> getSpuSaleAttrBySpuId(Integer spuId);
}
