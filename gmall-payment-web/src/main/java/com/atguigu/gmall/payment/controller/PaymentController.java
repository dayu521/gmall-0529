package com.atguigu.gmall.payment.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gmall.order.OrderInfo;
import com.atguigu.gmall.order.OrderService;
import com.atguigu.gmall.pay.AlipayRequestVo;
import com.atguigu.gmall.pay.PaymentInfo;
import com.atguigu.gmall.pay.PaymentService;
import com.atguigu.gmall.payment.config.GmallAlipayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Controller
public class PaymentController {

    @Reference
    OrderService orderService;

    @Reference
    PaymentService paymentService;


    /**
     * 更新订单状态信息
     * @return
     *
     *
     */
    @ResponseBody  //给支付宝回应success;以后就不会一个订单频繁通知
    @RequestMapping("/updateOrderStatus")
    public String updateOrderStatus(HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("支付宝异步回调....");
        //获取到订单的支付状态，修改订单状态

        //拿到阿里异步回调我们这个方法的所有参数
        Map<String, String> aliResponseMap = getAliResponseMap(request);
        
        //当前系统对外的订单号（商户订单号）
        String out_trade_no = aliResponseMap.get("out_trade_no");
        //支付宝交易号
        String trade_no = aliResponseMap.get("trade_no");
        //交易状态码
        String trade_status = aliResponseMap.get("trade_status");

        //1、验证合法性，验签
        try {
//            boolean signVerified = AlipaySignature
//                    .rsaCheckV1(aliResponseMap,
//                            GmallAlipayConfig.alipay_public_key,
//                            GmallAlipayConfig.charset,
//                    GmallAlipayConfig.sign_type); //调用SDK验证签名
//            //2、数据都是合法的我们可以继续操作
//            if(!signVerified){
//                //非法签名数据
//                log.info("非法的支付回调签名数据");
//                return "fail";
//            }
            //订单完成；商户已经处理。过了退款周期
            if(trade_status.equals("TRADE_SUCCESS")||trade_status.equals("TRADE_FINISHED")){
                //交易成功，修改订单order_info表中的订单信息
                //修改payment_info表中流水数据
                //1、修改订单状态信息
                orderService.updateOrderPaySuccess(out_trade_no);//将订单改为支付成功状态

                //2、修改payment_info流水信息;
                // alipay_trade_no payment_status callback_time callback_content
                PaymentInfo paymentInfo = new PaymentInfo();
                //流水信息修改
                paymentInfo.setAlipayTradeNo(trade_no);
                paymentInfo.setPaymentStatus(trade_status);
                paymentInfo.setCallbackTime(new Date());
                paymentInfo.setCallbackContent(JSON.toJSONString(aliResponseMap));
                paymentInfo.setOutTradeNo(out_trade_no);
                paymentService.updatePayement(paymentInfo);
            }

        } catch (Exception e) {
            log.info("验证签名失败：这是一个非法数据");
            return "fail";
        }

        return  "success";
    }


    /**
     * 查看订单列表
     * @return
     */
    @RequestMapping("/orderList")
    public String orderList(){

        //查看订单列表
        return  "redirect:http://order.gmall.com/list";
    }

    /**
     * 处理支付请求
     *
     * produces 告诉浏览器返回的是一个网页  response.setContentType();
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pay",produces = "text/html;charset=utf-8")
    public String paymentPage(AlipayRequestVo vo,Integer id) throws AlipayApiException {

        log.info("需要支付的请求信息是："+vo);
        //1、验证订单价格信息是否正确，如果不正确，非法提交重新去支付
        OrderInfo orderInfo = orderService.getOrderById(id);
        BigDecimal subtract = orderInfo.getTotalAmount().subtract(new BigDecimal(vo.getTotalAmount()));
        if(subtract.intValue() == 0){
            //价格一样
            //给阿里去发支付请求
            //1、获得初始化的AlipayClient
            AlipayClient alipayClient =
                    new DefaultAlipayClient(
                            GmallAlipayConfig.gatewayUrl,
                            GmallAlipayConfig.app_id,
                            GmallAlipayConfig.merchant_private_key,
                            "json",
                            GmallAlipayConfig.charset,
                            GmallAlipayConfig.alipay_public_key,
                            GmallAlipayConfig.sign_type);


            //2、设置请求参数
            AlipayTradePagePayRequest alipayRequest =
                    new AlipayTradePagePayRequest();

            //支付成功后需要跳到的用户页面
            alipayRequest.setReturnUrl(GmallAlipayConfig.return_url);
            //支付成功后异步的一个通知调用
            alipayRequest.setNotifyUrl(GmallAlipayConfig.notify_url);

            String payParam = JSON.toJSONString(vo);
            //3、构造请求参数
            alipayRequest.setBizContent("{\"out_trade_no\":\""+ vo.getOutTradeNo() +"\","
                    + "\"total_amount\":\""+ vo.getTotalAmount() +"\","
                    + "\"subject\":\""+ vo.getSubject() +"\","
                    + "\"body\":\""+ vo.getBody() +"\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

            //4、发送支付请求给阿里
            String result = alipayClient.pageExecute(alipayRequest).getBody();

            //5、创建出支付宝的流水
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setOutTradeNo(vo.getOutTradeNo());
            paymentInfo.setCreateTime(new Date());
            paymentInfo.setOrderId(id);
            paymentInfo.setPaymentStatus("TRADE_UNPAY");//未支付
            paymentInfo.setTotalAmount(new BigDecimal(vo.getTotalAmount()));
            paymentInfo.setSubject(vo.getSubject());

            //保存基本流水数据
            paymentService.createAliTrade(paymentInfo);
            return result;
        }else{
            return "订单非法";
        }

    }


    private Map<String,String> getAliResponseMap(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return  params;
    }
}
