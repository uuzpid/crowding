package com.pyx.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaMainVipStarter {

    public static void main(String[] args) {
        SpringApplication.run(EurekaMainVipStarter.class,args);
    }
}
