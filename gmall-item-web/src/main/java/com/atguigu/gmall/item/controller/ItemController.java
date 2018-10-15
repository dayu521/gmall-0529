package com.atguigu.gmall.item.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.sku.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;

    @RequestMapping("/{skuId}.html")
    public String itemPage(@PathVariable("skuId") Integer skuId, Model model){
        //1、查出sku的详细信息

        SkuInfo skuInfo = skuService.getSkuInfoBySkuId(skuId);
        model.addAttribute("skuInfo",skuInfo);
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
