package com.pyx;


import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;

// 启用 Hystrix 仪表盘功能
@EnableHystrixDashboard
@SpringBootApplication
public class DashboardStarter {
    public static void main(String[] args) {
        SpringApplication.run(DashboardStarter.class,args);
    }


}
