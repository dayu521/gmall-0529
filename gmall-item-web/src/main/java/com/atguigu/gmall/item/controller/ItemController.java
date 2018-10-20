package com.atguigu.gmall.item.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.manager.SkuEsService;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.sku.SkuAttrValueMappingTo;
import com.atguigu.gmall.manager.sku.SkuInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class ItemController {

    @Reference
    SkuService skuService;


    /**
     * 我们可以写一个拦截器，在请求达到目标方法的时候，看看方法是否需要登陆才能访问，如果需要就进行登陆操作
     * @return
     */
    @LoginRequired
    @RequestMapping("/haha")
    public String hahaha(HttpServletRequest request){
        //常用的key都要抽取为常量
        Map<String,Object> userInfo = (Map<String, Object>) request.getAttribute("userInfo");

        //要用户名
        //1、如果这个token不是没意义的随机数（只用来做redis中key标识的）
        //2、假设这个token是 一串有意义的  dsjakljdsalkjdals.djsaljdaskljdlasjdkslajdsadlkas
        //   这串数据以及包含了你最常用的信息，你要用这些信息不用查了，你的领牌里面就有
        //不可伪造;还携带了常用信息
        //JWT(JSON Web Token)（规范）稍微加密。能加也要能解
        //UserInfo = redis.get(token)
        log.info("我们可以解码到用户的信息是：{}",userInfo);
        return "haha";
    }




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

        //3、增加点击率；更新es的hotScore值
        //redis  把redis中这个商品的热度保存起来增加即可
        skuService.incrSkuHotScore(skuId);


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
