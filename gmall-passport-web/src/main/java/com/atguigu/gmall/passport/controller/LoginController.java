package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gmall.passport.utils.JwtUtils;
import com.atguigu.gmall.user.UserInfo;
import com.atguigu.gmall.user.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
public class LoginController {

    @Reference
    UserInfoService userInfoService;

    /**
     * 以后要登陆全部跳到http://www.gmallsso.com/loginPage.html制证
     * 去登陆页面
     * @return
     */

    @RequestMapping("/login")
    public String login(UserInfo userInfo, String originUrl,
                        @CookieValue(name = CookieConstant.SSO_COOKIE_NAME,required = false)
                                String token,
                        HttpServletResponse response){

        //1、如果说cookie也没东西，userInfo也没东西。这个人直接访问登陆页
        if(StringUtils.isEmpty(token)&&userInfo.getLoginName()==null){
            return "index";
        }

        //1、登陆过了
        if(!StringUtils.isEmpty(token)){
            //都已经登陆过了就重定向到那个人那里
            return "redirect:"+originUrl+"?token="+token;
        }else{
            //2、没有登陆过
            if(StringUtils.isEmpty(userInfo.getLoginName())){
                //去登陆页
                return "index";
            }else{
                //用户填写了用户信息
                UserInfo login = userInfoService.login(userInfo);
                Map<String,Object> body = new HashMap<>();
                body.put("id",login.getId());
                body.put("loginName",login.getLoginName());
                body.put("nickName",login.getNickName());
                body.put("headImg",login.getHeadImg());
                body.put("email",login.getEmail());

                String newToken = JwtUtils.createJwtToken(body);
                if(login!=null){
                    //登陆成功。回到原始地方
                    //本sso域也得在cookie中保存令牌
                    Cookie cookie = new Cookie(CookieConstant.SSO_COOKIE_NAME, newToken);
                    cookie.setPath("/"); //无论当前网站那一层都能用
                    response.addCookie(cookie);
                    //登陆成了将你的所有信息放在redis中，
                    //redis.set(newToken,loginJson)
                    if(!StringUtils.isEmpty(originUrl)){
                        return "redirect:"+originUrl+"?token="+newToken;
                    }else{
                        //登陆成了到首页
                        return "redirect:http://www.gmall.com";
                    }

                }else{
                    //登陆失败，继续登陆
                    return "index";
                }
            }

        }



    }


    @ResponseBody
    @RequestMapping("/confirmToken")
    public String confirmToken(String token){
        boolean b = JwtUtils.confirmJwtToken(token);
        return b?"ok":"error";
    }
}
