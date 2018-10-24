package com.atguigu.gmall.pay;

/**
 * 支付相关服务
 */
public interface PaymentService {

    void createAliTrade(PaymentInfo paymentInfo);

    void updatePayement(PaymentInfo paymentInfo);
}
