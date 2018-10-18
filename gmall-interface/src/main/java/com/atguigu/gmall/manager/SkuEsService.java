package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.es.SkuSearchParamEsVo;
import com.atguigu.gmall.manager.es.SkuSearchResponseEsVo;

import java.util.List;

public interface SkuEsService {

    /**
     * 商品上架
     * @param skuId
     */
    void onSale(Integer skuId);

    /**
     * 搜索获取数据
     * @param vo
     * @return
     */
    SkuSearchResponseEsVo searchSkuInfoFromEs(SkuSearchParamEsVo vo);
}
