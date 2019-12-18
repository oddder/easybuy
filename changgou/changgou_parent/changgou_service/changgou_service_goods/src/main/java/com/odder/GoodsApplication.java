package com.odder;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Steven
 * @description com.changgou.goods
 */
@SpringBootApplication
@EnableEurekaClient //开启Eureka客户端服务发现
@MapperScan(basePackages = "com.odder.goods.dao")
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }
}
