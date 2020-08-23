package com.pyx.crowd.mvc.handler;

import com.pyx.crowd.entity.Admin;
import com.pyx.crowd.service.api.AdminService;
import com.pyx.crowd.util.CrowdUtil;
import com.pyx.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestHandler {

    Logger logger = LoggerFactory.getLogger(TestHandler.class);


    @Autowired
    private AdminService adminService;

    @ResponseBody
    @RequestMapping("/send/array/one.html")
    public String testReceiveArrayOne(@RequestParam("array[]")List<Integer> array){
        for (Integer integer : array) {
            System.out.println("number="+integer);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/array/two.html")
    public String testReceiveArrayTwo(ParamData paramData){
        List<Integer> array = paramData.getArray();
        for (Integer integer : array) {
            System.out.println("number="+integer);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/array/three.html")
    public String testReceiveArrayThree(@RequestBody List<Integer> array){
        for (Integer integer : array) {
            logger.info("integer="+integer);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/array/object.json")
    public ResultEntity<Student> testReceiveComposeObject(@RequestBody Student student,HttpServletRequest request){
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult="+judgeResult);
        logger.info(""+student);
        return ResultEntity.successWithData(student);
    }

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest request){
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult="+judgeResult);
        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList",adminList);

        return "target";
    }
}
