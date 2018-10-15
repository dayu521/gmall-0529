package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.spu.BaseSaleAttr;
import com.atguigu.gmall.manager.spu.SpuImage;
import com.atguigu.gmall.manager.spu.SpuInfo;

import java.util.List;

public interface SpuInfoService {
    List<SpuInfo> getSpuInfoByC3Id(Integer catalog3Id);

    List<BaseSaleAttr> getBaseSaleAttr();

    //spuInfo的大保存
    void saveBigSpuInfo(SpuInfo spuInfo);

    /**
     * 查询spu的所有图片，以供sku进行选中
     * @param spuId
     * @return
     */
    List<SpuImage> getSpuImages(Integer spuId);
}
