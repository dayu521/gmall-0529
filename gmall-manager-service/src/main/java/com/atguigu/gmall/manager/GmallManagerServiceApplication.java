package com.atguigu.gmall.manager;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//扫描所有mybatis的mapper文件
@EnableTransactionManagement
@EnableDubbo  //开启dubbo
@MapperScan("com.atguigu.gmall.manager.mapper")
@SpringBootApplication
public class GmallManagerServiceApplication {

	/**
	 * 即使不依赖web容器启动起来dubbo会自动阻塞主线程，防止主线程停止，dubbo停止
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(GmallManagerServiceApplication.class, args);
	}
}
