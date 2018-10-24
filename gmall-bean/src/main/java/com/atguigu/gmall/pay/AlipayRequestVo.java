package com.atguigu.gmall.pay;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlipayRequestVo implements Serializable{
	
	private String outTradeNo;//订单号
	private String subject;//订单名字
	private String totalAmount;//订单需要支付的总额
	private String body;//订单的描述
	
	private String productCode = "FAST_INSTANT_TRADE_PAY";


	
	

	
	

}
