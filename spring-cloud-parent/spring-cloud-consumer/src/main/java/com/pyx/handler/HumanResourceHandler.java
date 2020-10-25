package com.pyx.handler;

import com.pyx.spring.cloud.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HumanResourceHandler {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/consumer/get/employee")
    public Employee getEmployeeRemote() {
        // 远程调用方法的主机地址
        // String host = "http://localhost:1000";

        // 启动eureka后
        // 将远程微服务调用地址从“IP地址+端口号”改成微服务名称
        String host = "http://provider1";
        // 远程调用方法的具体 URL 地址
        String url = "/provider/get/employee/remote";
        return restTemplate.getForObject(host + url, Employee.class);
    }
}
