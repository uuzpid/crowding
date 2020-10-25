package com.pyx.spring.cloud.api;

import com.pyx.spring.cloud.entity.Employee;
import com.pyx.spring.cloud.factory.MyFallBackFactory;
import com.pyx.spring.cloud.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// value属性为要调用的远程微服务的名称
// fallbackFactory 服务降级
@FeignClient(value = "provider1",fallbackFactory = MyFallBackFactory.class)
public interface EmployeeRemoteService {

    // 远程调用的接口方法 要求与原来的方法完全一致
    @RequestMapping("/provider/get/employee/remote")
    public Employee getEmployeeRemote();

    @RequestMapping("/provider/get/emp/list/remote")
    List<Employee> getEmpListRemote(@RequestParam("keyword") String keyword);

    @RequestMapping("/provider/get/emp/with/circuit/breaker")
    public ResultEntity<Employee> getEmpWithCircuitBreaker(@RequestParam("signal")String signal);
}
