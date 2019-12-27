package com.odder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/24 10:29
 * @Version 1.0
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.odder.content.dao"})
public class ContentApplicaiton {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplicaiton.class,args);
    }
}
