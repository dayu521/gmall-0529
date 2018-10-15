package com.atguigu.gmall.manager.mapper.sku;

import com.atguigu.gmall.manager.sku.SkuAllSaveAttrAndValueTo;
import com.atguigu.gmall.manager.sku.SkuImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SkuImageMapper extends BaseMapper<SkuImage> {


    /**
     * 给页面获取sku对应的所有能供选择的销售属性以及当前sku是哪个销售属性的大对象
     * @param id
     * @param spuId
     * @return
     */
    List<SkuAllSaveAttrAndValueTo> getSkuAllSaveAttrAndValue(@Param("id") Integer id,@Param("spuId") Integer spuId);
}
