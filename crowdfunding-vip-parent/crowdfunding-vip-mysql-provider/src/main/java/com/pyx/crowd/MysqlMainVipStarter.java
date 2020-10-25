package com.pyx.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.pyx.crowd.mapper")
@SpringBootApplication
public class MysqlMainVipStarter {

    public static void main(String[] args) {
        SpringApplication.run(MysqlMainVipStarter.class,args);
    }
}
