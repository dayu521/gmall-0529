package com.atguigu.gmall.cart.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.cart.CartVo;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gmall.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Reference
    CartService cartService;

    public String otherFunction(){

        //TODO  1、购物车的商品有很多，我之前选中的商品要记住
        //TODO  2、购物车的商品加减
        //TODO  3、购物车的商品删除
        //TODO  4、清空购物车
        //TODO 以上怎么做？都用ajax
        return "";
    }



    /**
     * 查询购物车的数据
     * @return
     */
    @LoginRequired(needLogin = false) //只要用户数据不用强制登陆
    @RequestMapping("/cartList")
    public String cartInfoPage(HttpServletRequest request,HttpServletResponse response){

        Map<String,Object> userInfo = (Map<String, Object>) request.getAttribute(CookieConstant.LOGIN_USER_INFO_KEY);
        //判断是否需要合并购物车？
        //temp:cart:9c766092f
        String tempCart = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
        if(!StringUtils.isEmpty(tempCart) && userInfo!=null){
            //说明有临时购物车。合并购物车
            cartService.mergeCart(tempCart,Integer.parseInt(userInfo.get("id").toString()));
            Cookie cookie = new Cookie(CookieConstant.COOKIE_CART_KEY, "372863287");
            cookie.setMaxAge(0);
            //立即删除之前临时购物车数据
            response.addCookie(cookie);
        }

        boolean login = false;
        String cartKey = "";
        if(userInfo!=null){
            //1、登陆了
            login = true;
            cartKey = userInfo.get("id").toString()+"";

        }else{
            //2、没登录
            login = false;
            cartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
        }

        //查询数据
        List<CartItem> cartItems =  cartService.getCartInfoList(cartKey,login);

        //购物车
        CartVo cartVo = new CartVo();
        cartVo.setCartItems(cartItems);
        cartVo.setTotalPrice(cartVo.getTotalPrice());


        request.setAttribute("cartVo",cartVo);
        //来到列表页
        return "cartList";
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
                            HttpServletResponse response) throws InterruptedException {


        //判断是否登陆，登陆了用user:cart:12:info在redis中
        Map<String,Object> loginUser = (Map<String, Object>) request.getAttribute(CookieConstant.LOGIN_USER_INFO_KEY);
        String cartKey = null;
        if(loginUser == null){
            cartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
            //未登陆情况下的处理
            if(StringUtils.isEmpty(cartKey)){
                //返回的是给你造的购物车在redis'中存数据用的key。
                cartKey = cartService.addToCartUnLogin(skuId,null,num);
                Cookie cookie = new Cookie(CookieConstant.COOKIE_CART_KEY, cartKey);
                cookie.setMaxAge(CookieConstant.COOKIE_CART_KEY_MAX_AGE);
                response.addCookie(cookie);
            }else {
                String cartId = cartService.addToCartUnLogin(skuId, cartKey, num);

            }
        }else{
            Integer userId = Integer.parseInt(loginUser.get("id").toString());
            //合并购物车；
            cartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
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
        //把购物车刚才的数据查出来 购物车的id，查那个
        CartItem cartItem = cartService.getCartItemInfo(cartKey,skuId);
        request.setAttribute("skuInfo",cartItem);
        return "success";
    }
}
