package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.spu.BaseSaleAttr;
import com.atguigu.gmall.manager.spu.SpuInfo;

import java.util.List;

public interface SpuInfoService {
    List<SpuInfo> getSpuInfoByC3Id(Integer catalog3Id);

    List<BaseSaleAttr> getBaseSaleAttr();

    //spuInfo的大保存
    void saveBigSpuInfo(SpuInfo spuInfo);
}
