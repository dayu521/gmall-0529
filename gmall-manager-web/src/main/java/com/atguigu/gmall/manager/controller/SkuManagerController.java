package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.SpuInfoService;
import com.atguigu.gmall.manager.spu.SpuSaleAttr;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/sku")
@Controller
public class SkuManagerController {

    @Reference
    SkuService skuService;


    /**
     * 获取某个spu下的所有平台属性
     * @param catalog3Id
     * @return
     */
    @ResponseBody
    @RequestMapping("/base_attr")
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(@RequestParam("id") Integer catalog3Id){
        return  skuService.getBaseAttrInfoByCatalog3Id(catalog3Id);
    }

    /**
     *
     * @param spuId
     * @return
     */
    @ResponseBody
    @RequestMapping("/spu_sale_attr")
    public List<SpuSaleAttr> getSpuSaleAttrBySpuId(@RequestParam("id") Integer spuId){
        return skuService.getSpuSaleAttrBySpuId(spuId);
    }
}
