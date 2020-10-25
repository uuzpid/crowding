package com.pyx.spring;

import com.pyx.spring.entity.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 表示当前类是一个配置类，作用相当于spring的配置文件
@Configuration
public class MyAnnotationConfiguration {

    // @Bean 注解相当于做了下面的xml标签配置，把方法的返回值放入IOC容器
    // <bean id="emp" class="com.pyx.spring.entity.Employee">
    @Bean
    public Employee getEmployee(){
        return new Employee();
    }
}
