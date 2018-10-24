package com.atguigu.gmall.payment;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.atguigu.gmall.payment.mapper")
@EnableAsync
@EnableDubbo
@SpringBootApplication
public class GmallPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallPaymentServiceApplication.class, args);
	}
}
