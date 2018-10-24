package com.atguigu.gmall.pay;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付流水信息记录
 */
@Data
public class PaymentInfo extends SuperBean {

    // total_amount  subject  payment_status  create_time  callback_time  callback_content
    private String outTradeNo;
    private Integer orderId;
    private String alipayTradeNo;//支付宝流水好
    private BigDecimal totalAmount;
    private String subject;//订单的描述
    private String paymentStatus; //TRADE_SUCCESS TRADE_FINISHED
    private Date createTime;
    private Date callbackTime;//支付宝回调时间
    private String callbackContent;//支付宝回调内容
}
