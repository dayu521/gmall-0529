package com.atguigu.gmall.cart.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gmall.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class CartController {

    @Reference
    CartService cartService;

    @LoginRequired(needLogin = true)
    @ResponseBody
    @RequestMapping("/oo")
    public String oooo(){
        return "ok";
    }

    /**
     *
     * @param skuId  哪个商品
     * @param num   加几个
     * @return
     */

    @LoginRequired(needLogin = false)
    @RequestMapping("/addToCart")
    public String addToCart(Integer skuId, Integer num,
                            HttpServletRequest request,
                            HttpServletResponse response){


        //判断是否登陆，登陆了用user:cart:12:info在redis中
        Map<String,Object> loginUser = (Map<String, Object>) request.getAttribute(CookieConstant.LOGIN_USER_INFO_KEY);
        if(loginUser == null){
            String cartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
            //为登陆情况下的处理
            if(StringUtils.isEmpty(cartKey)){
                //返回的是给你造的购物车在redis'中存数据用的key
                String cartId = cartService.addToCartUnLogin(skuId,null,num);
                response.addCookie(new Cookie(CookieConstant.COOKIE_CART_KEY,cartId));
            }else {
                String cartId = cartService.addToCartUnLogin(skuId, cartKey, num);

            }
        }else{
            Integer userId = Integer.parseInt(loginUser.get("id").toString());
            //合并购物车；
            String cartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
            if(StringUtils.isEmpty(cartKey)){
                //cookie没有临时购物车
                cartService.addToCartLogin(skuId,userId,num);
            }else {
                //有临时购物车，先合并在加购物车
                cartService.mergeCart(cartKey,userId);
                cartService.addToCartLogin(skuId,userId,num);
                //删掉cart-key这个cookie
                CookieUtils.removeCookie(response,CookieConstant.COOKIE_CART_KEY);
            }
        }
        //没登录，临时搞一个购物车的id了;response.addCookie("cart-key":"dsajldjaskldj")
        //这个id在redis中存数据

        return "success";
    }
}
