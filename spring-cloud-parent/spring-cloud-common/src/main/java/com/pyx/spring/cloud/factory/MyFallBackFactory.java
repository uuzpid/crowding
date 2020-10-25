package com.pyx.spring.cloud.factory;

import com.pyx.spring.cloud.api.EmployeeRemoteService;
import com.pyx.spring.cloud.entity.Employee;
import com.pyx.spring.cloud.util.ResultEntity;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 实现consumer端服务降级的功能
 * 实现FallbackFactory接口时要传入@FeignClient标注的接口类型
 * 在create方法中返回@FeignClient标注的接口类型对象，当provider调用失败后，会执行这个对象的对应方法
 */
@Component
public class MyFallBackFactory implements FallbackFactory<EmployeeRemoteService> {
    public EmployeeRemoteService create(final Throwable throwable) {
        return new EmployeeRemoteService() {
            public Employee getEmployeeRemote() {
                return null;
            }

            public List<Employee> getEmpListRemote(String keyword) {
                return null;
            }

            public ResultEntity<Employee> getEmpWithCircuitBreaker(String signal) {
                return ResultEntity.failed(throwable.getMessage());
            }
        };
    }
}
