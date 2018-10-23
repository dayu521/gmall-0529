package com.atguigu.gmall.order.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.cart.CartVo;
import com.atguigu.gmall.order.OrderService;
import com.atguigu.gmall.order.TradePageVo;
import com.atguigu.gmall.user.UserAddress;
import com.atguigu.gmall.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 交易结算
 */
@Controller
public class TradeController {



    @Reference
    CartService cartService;

    @Reference
    UserService userService;

    @Reference
    OrderService orderService;


    @LoginRequired //必须登录的
    @RequestMapping("/trade")
    public String trade(HttpServletRequest request){
        //1、判断用户选中的商品（验证商品），没有选中，还是返回购物车页面
        //1.1、获取到用户信息
        Map<String,Object> userInfo = (Map<String, Object>) request.getAttribute("userInfo");
        int id = Integer.parseInt(userInfo.get("id").toString());
        //1.2、获取购物车被选中的商品
        List<CartItem> cartItemList = cartService.getCartInfoCheckedList(id);


        //2、查询和展示收货人信息
        List<UserAddress> userAddresses = userService.getUserAddressesByUserId(id);


        //3、展示购物车信息
        TradePageVo vo = new TradePageVo();
        CartVo cartVo = new CartVo();
        cartVo.setCartItems(cartItemList);
        BigDecimal totalPrice = cartVo.getTotalPrice();
        vo.setTotalPrice(totalPrice);
        vo.setCartItems(cartItemList);
        vo.setUserAddresses(userAddresses);


        //3、防重复提交的；生成一个令牌，服务器一份，页面一份
        String token = orderService.createTradeToken();//创建一个交易令牌，服务端也保存了


        //跳到结算页
        request.setAttribute("tradeInfo",vo);
        request.setAttribute("token",token);

        return "trade";
    }
}
