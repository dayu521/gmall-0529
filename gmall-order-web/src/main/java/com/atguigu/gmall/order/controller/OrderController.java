package com.atguigu.gmall.order.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.order.OrderInfoTo;
import com.atguigu.gmall.order.OrderService;
import com.atguigu.gmall.order.OrderSubmitVo;
import com.atguigu.gmall.user.UserAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
public class OrderController {



    @Reference
    OrderService orderService;

    /**
     * 防重复提交
     * 令牌机制：
     * 1、当前页直接刷新(之前提交过的所有数据再来一次)
     * 2、回退到上一步页面，再来一次(之前提交过的所有数据再来一次)
     * 共同点：之前提交过的所有数据再来一次
     * 令牌：
     *      每次提交带唯一令牌，令牌使用后就删除
     *      1）、页面提交的时候上次可能给页面放了个令牌，提交上来，判断这个令牌第一次用，执行方法
     *          有一次提交还带着老令牌；不要
     *      2）、只要来到这页面给他创建一个令牌，除了刷新此页面会更新令牌。回退浏览器是显示上次页面的所有缓存内容。
     *          令牌不会更替，再提交还是老令牌
     *
     *
     * @param submitVo
     * @return
     */
    @LoginRequired
    @RequestMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo submitVo, HttpServletRequest request) throws IOException {

        Map<String,Object> userInfo = (Map<String, Object>) request.getAttribute("userInfo");

        log.info("当前用户是：",userInfo);
        //4、都验证通过可以生成订单
        //LastStep：生成订单；生成OrderInfo；OrderItem
        log.info("页面收到的数据：{}",submitVo);

        //1、只收页面提交的两个数据
        //2、防重复提交
        boolean token = orderService.verfyToken(submitVo.getTradeToken());
        if(!token){
            //令牌失效
            request.setAttribute("errorMsg","订单信息失效，请去购物车重新刷新并下单");
            return "tradeFail";
        }

        //3、验证库存；库存失败就来到失败页
        Integer userId = Integer.parseInt(userInfo.get("id")+"");
        List<String> stockNotGou = orderService.verfyStock(userId);
        if(stockNotGou!=null && stockNotGou.size()>0){
            //令牌失效
            String string = JSON.toJSONString(stockNotGou);
            request.setAttribute("errorMsg","购物车中商品库存不足："+string);
            return "tradeFail";
        }

        //4、以上都ok,下单
        OrderInfoTo orderInfoTo = new OrderInfoTo();
        orderInfoTo.setOrderComment(submitVo.getOrderComment());
        Integer userAddressId = submitVo.getUserAddressId();
        UserAddress userAddress = orderService.getUserAddressById(userAddressId);
        orderInfoTo.setConsignee(userAddress.getConsignee());
        orderInfoTo.setConsigneeTel(userAddress.getPhoneNum());
        orderInfoTo.setDeliveryAddress(userAddress.getUserAddress());

        //创建订单
        try {
            orderService.createOrder(userId,orderInfoTo);
        }catch (Exception e){
            request.setAttribute("errorMsg","网络异常..."+e.getMessage());
            e.printStackTrace();
            return  "tradeFail";
        }

        //成功来到支付页，先写成list
        return "list";
    }
}
