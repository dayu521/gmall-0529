package com.atguigu.gmall.item.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.SkuEsService;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.sku.SkuAttrValueMappingTo;
import com.atguigu.gmall.manager.sku.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;




    @RequestMapping("/{skuId}.html")
    public String itemPage(@PathVariable("skuId") Integer skuId, Model model, HttpServletRequest request){
        //1、查出sku的详细信息
        //2、service应该使用缓存机制
        SkuInfo skuInfo = null;
        try {
            skuInfo = skuService.getSkuInfoBySkuId(skuId);
            if(skuInfo == null){
                //跳转到商品不存在页
            }
        } catch (InterruptedException e) {
            //
        }
        model.addAttribute("skuInfo",skuInfo);

        //缓存一下？业务
        //先判断缓存有没有

        /**
         * sku_id  spu_id  sku_name                  sale_attr_value_id  sale_attr_value_name
         ------  ------  ------------------------  ------------------  ----------------------
         29      55  (NULL)                    118,117             裸机版,黑色
         30      55  联想s60银色套餐一                119,116             套餐一,红色
         */
        Integer spuId = skuInfo.getSpuId();
        //2、查出当前sku对应的spu下面所有sku销售属性值的组合
        List<SkuAttrValueMappingTo> valueMappingTos = skuService.getSkuAttrValueMapping(spuId);
        model.addAttribute("skuValueMapping",valueMappingTos);
        return "item";
    }

//      测试语法
//      @RequestMapping("/{skuId}.html")
//    public String itemPage(@PathVariable("skuId") Integer skuId, Model model){
//        model.addAttribute("divId",22);
//        model.addAttribute("msg","哈哈，你好");
//
//        Map<String,Object> map1 = new HashMap<>();
//        map1.put("id",100);
//        map1.put("stuName","张三1");
//
//
//        Map<String,Object> map2 = new HashMap<>();
//        map2.put("id",101);
//        map2.put("stuName","张三2");
//
//
//        Map<String,Object> map3 = new HashMap<>();
//        map3.put("id",102);
//        map3.put("stuName","<h1>张三3</h1>");
//
//        model.addAttribute("students", Arrays.asList(map1,map2,map3));
//        //request.setAttribute()
//        return "item";
//    }

}
