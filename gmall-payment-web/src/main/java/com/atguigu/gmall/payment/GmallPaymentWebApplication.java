package com.atguigu.gmall.payment;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class GmallPaymentWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallPaymentWebApplication.class, args);
	}
}
