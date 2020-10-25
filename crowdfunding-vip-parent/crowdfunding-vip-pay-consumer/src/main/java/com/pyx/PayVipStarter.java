package com.pyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PayVipStarter {
    public static void main(String[] args) {
        SpringApplication.run(PayVipStarter.class,args);
    }
}
