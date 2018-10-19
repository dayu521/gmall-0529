package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.es.SkuSearchParamEsVo;
import com.atguigu.gmall.manager.es.SkuSearchResultEsVo;

public interface SkuEsService {

    /**
     * 商品上架
     * @param skuId
     */
    void onSale(Integer skuId);

    SkuSearchResultEsVo searchSkuFromES(SkuSearchParamEsVo paramEsVo);

    /**
     * 更新Es中商品的热度值
     * @param skuId
     * @param hincrBy
     */
    void updateHotScore(Integer skuId, Long hincrBy);
}
