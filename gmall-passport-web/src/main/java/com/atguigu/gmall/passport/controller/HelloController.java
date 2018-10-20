package com.atguigu.gmall.passport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/ok")
    public String hello(){
        return "ok";
    }
}
