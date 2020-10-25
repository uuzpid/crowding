package com.pyx.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

 // 所有的注册中心都可以用这个注解 例如Nacos @EnableDiscoveryClient
@EnableEurekaServer
@SpringBootApplication
public class EurekaMainType {
    public static void main(String[] args) {
        SpringApplication.run(EurekaMainType.class,args);
    }
}
