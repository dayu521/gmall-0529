package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.spu.SpuSaleAttr;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/sku")
@RestController
public class SkuController {

    @Reference
    SkuService skuService;

    /**
     * 按照三级分类查出他下面所有的平台属性名以及他的值
     * @param catalog3Id
     * @return
     */
    @RequestMapping("/base_attr_info.json")
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(@RequestParam("id") Integer catalog3Id){

        return skuService.getBaseAttrInfoByCatalog3Id(catalog3Id);
    }

    /**
     * 查询spuId对应的所有可供选择的sku
     * @param spuId
     * @return
     */
    @RequestMapping("/spu_sale_attr.json")
    public List<SpuSaleAttr> getSpuSaleAttrBySpuId(@RequestParam("id") Integer spuId){
        return skuService.getSpuSaleAttrBySpuId(spuId);
    }
}
