package com.atguigu.gmall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/spu")
@Controller
public class SpuManagerController {

    @RequestMapping("/listPage.html")
    public String listPage(){
        return "spu/spuListPage";
    }
}
