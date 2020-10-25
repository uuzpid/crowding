package com.pyx.crowd;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ProjectOrderMain {
    public static void main(String[] args) {
        SpringApplication.run(ProjectOrderMain.class,args);
    }
}
