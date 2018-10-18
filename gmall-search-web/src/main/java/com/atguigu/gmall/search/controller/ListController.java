package com.atguigu.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.SkuEsService;
import com.atguigu.gmall.manager.es.SkuSearchParamEsVo;
import com.atguigu.gmall.manager.es.SkuSearchResponseEsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class ListController {

    @Reference
    SkuEsService skuEsService;

    @RequestMapping("/list.html")
    public String  listPage(SkuSearchParamEsVo vo, Model model){
        log.debug("sku查询的所有数据："+vo);
        SkuSearchResponseEsVo searchResponseEsVo = skuEsService.searchSkuInfoFromEs(vo);


        model.addAttribute("searchResult",searchResponseEsVo);
        return "list";
    }

}
