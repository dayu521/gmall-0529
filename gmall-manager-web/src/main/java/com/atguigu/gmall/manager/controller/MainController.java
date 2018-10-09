package com.atguigu.gmall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/main")
    public String hello(){

        //ThymeleafProperties 这里是thymeleaf所有的默认配置
        //要去哪个页面的地址
        /**
         * 	public static final String DEFAULT_PREFIX = "classpath:/templates/";
         public static final String DEFAULT_SUFFIX = ".html";
         */
        //由视图解析器拼串得到地址：前缀。后缀
        return "main";
    }
}
