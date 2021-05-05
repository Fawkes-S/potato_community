package com.potato.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@SpringBootApplication
@EnableEurekaClient
public class SearchApplication {
    /**
     * 搜索微服务
     */
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class,args);
    }

    /**
     * 初始化idWork对象
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}