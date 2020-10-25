package com.pyx.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SpringCloudConfig {

    // 注入IOC容器 注解版 替换了之前的<bean>
    @Bean
    @LoadBalanced // 启用eureka后需要加这个注解负载均衡
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
