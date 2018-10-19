package com.atguigu.gmall.search;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class GmallSearchWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallSearchWebApplication.class, args);
	}
}
