package com.atguigu.gmall.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.SpuInfoService;
import com.atguigu.gmall.manager.spu.SpuInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/spu")
public class SpuManagerController {

    @Reference
    SpuInfoService spuInfoService;

    /**
     * controller
     * 1、收集和处理页面提交来的数据
     *      处理：将vo的数据封装到bean中
     * 2、将要处理的数据直接传给service，service来完成功能
     * 3、接受service处理完后的值，返回给页面
     * @param catalog3Id
     * @return
     */
    @ResponseBody
    @RequestMapping("/info.json")
    public List<SpuInfo> getSpuInfoByC3Id(@RequestParam("catalog3Id") Integer catalog3Id){


        return spuInfoService.getSpuInfoByC3Id(catalog3Id);
    }


    @RequestMapping("/listPage.html")
    public String spuListPage(){

        return "spu/spuListPage";
    }
}
