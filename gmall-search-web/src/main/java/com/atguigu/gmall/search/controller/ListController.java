package com.atguigu.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.manager.SkuEsService;
import com.atguigu.gmall.manager.es.SkuSearchParamEsVo;
import com.atguigu.gmall.manager.es.SkuSearchResultEsVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ListController {

    @Reference
    SkuEsService skuEsService;



    @LoginRequired  //需要登陆
    @RequestMapping("/hehe")
    public String hehe(){
        //想要用户信息
        //eyJoZWFkSW1nIjoiMyIsIm5pY2tOYW1lIjoi5byg5LiJIiwibG9naW5OYW1lIjoiemhhbmdzYW4iLCJpZCI6MywiZW1haWwiOiJhdGd1aWd1LmNvbSJ9
        //以上解码就是用户信息


        return "hehe";
    }

    /**
     * 将所有页面可能提交的查询数据封装入参
     * @param paramEsVo
     * @return
     */
    @RequestMapping("/list.html")
    public String  listPage(SkuSearchParamEsVo paramEsVo, Model model){
        //按照keyword -xxxxx

        //搜索完成以后返回这个对象，这个对象里面有所有的数据；
        SkuSearchResultEsVo searchResult = skuEsService.searchSkuFromES(paramEsVo);
        model.addAttribute("searchResult",searchResult);
        return "list";
    }

}
