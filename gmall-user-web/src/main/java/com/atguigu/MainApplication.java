package com.atguigu;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableDubbo
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MainApplication {


    /**
     * 1、各层引入哪些依赖
     * 2、包冲突的排除
     *
     *
     * 问题：
     * 1）、dubbo的版本 2.6.2
     * 2）、zk要先禁用vm的网卡
     * 依赖的排除（dubbo里面排掉所有的日志包）
     * github的提交流程
     * 3）、测试的时候排除掉数据源的自动配置
     *    @EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
         @SpringBootConfiguration
         @ComponentScan("com.atguigu.service")


         第二种 @SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
     * 4)、测试注意dubbo用 @EnableDubbo 开启功能
     *
     *
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class,args);
    }
}
