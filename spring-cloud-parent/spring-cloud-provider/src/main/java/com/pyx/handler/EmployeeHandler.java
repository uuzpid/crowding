package com.pyx.handler;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.pyx.spring.cloud.entity.Employee;
import com.pyx.spring.cloud.util.ResultEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EmployeeHandler {

    @RequestMapping("/provider/get/employee/remote")
    public Employee getEmployeeRemote() {

        return new Employee(555, "tom555", 555.55);
    }

    @RequestMapping("/provider/get/emp/list/remote")
    List<Employee> getEmpListRemote(@RequestParam("keyword") String keyword){

        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList.add(new Employee(33,"empName33",333.33));
        employeeList.add(new Employee(44,"empName44",444.44));
        employeeList.add(new Employee(55,"empName55",555.55));

        return employeeList;
    }

    // 注解通过 fallbackMethod 属性指定断路情况下要调用的备份方法
    @HystrixCommand(fallbackMethod = "getEmpWithCircuitBreakerBackup")
    @RequestMapping("/provider/get/emp/with/circuit/breaker")
    public ResultEntity<Employee> getEmpWithCircuitBreaker(@RequestParam("signal")String signal) throws InterruptedException {
        if("quick-bang".equals(signal)){
            throw new RuntimeException();
        }
        if("slow-bang".equals(signal)){
            Thread.sleep(5000);
        }
        return ResultEntity.successWithData(new Employee(66,"empName66",66.66));
    }

    public ResultEntity<Employee> getEmpWithCircuitBreakerBackup(@RequestParam("signal")String signal){
        String message = "方法执行出现问题，断路 signal="+signal;
        return ResultEntity.failed(message);
    }

/*    @RequestMapping("/provider/get/employee/remote")
    public Employee getEmployeeRemote(HttpServletRequest request) {

        int serverPort = request.getServerPort();

        return new Employee(555, "tom555"+serverPort, 555.55);
    }*/
}
