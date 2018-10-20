package com.atguigu.gmall.search.config;

import com.atguigu.gmall.interceptor.LoginRequireInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Import(LoginRequireInterceptor.class)   //导入到ioc容器中，其他人都能用
@Configuration
public class SearchWebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    LoginRequireInterceptor loginRequireInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(loginRequireInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("*.jpg");
    }
}
