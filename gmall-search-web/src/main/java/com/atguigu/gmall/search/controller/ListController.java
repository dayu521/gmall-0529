package com.atguigu.gmall.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ListController {

    @RequestMapping("/list.html")
    public String  listPage(){
        return "list";
    }

}
