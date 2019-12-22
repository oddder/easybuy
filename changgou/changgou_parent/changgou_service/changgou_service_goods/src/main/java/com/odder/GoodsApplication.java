package com.odder;


import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
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

    /**
     * 注册IdWorker实例对象
     * @return
     */
    @Bean
    public IdWorker getIdWorker(){
        //IdWorker(工作机器id,数据中心id)
        return new IdWorker(0, 0);
    }

}
