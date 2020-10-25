package com.pyx.crowd.handler;

import com.pyx.crowd.api.MySQLRemoteService;
import com.pyx.crowd.constant.CrowdConstant;
import com.pyx.crowd.util.ResultEntity;
import com.pyx.crowd.vo.PortalTypeVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class PortalHandler {

    @Resource
    private MySQLRemoteService mySQLRemoteService;


    @RequestMapping("/")
    public String showPortalPage(Model model){

        // 调用mySQLRemoteService提供的方法查询首页显示的数据
        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();

        // 检查查询结果
        String result = resultEntity.getResult();
        if(ResultEntity.SUCCESS.equals(result)){
            // 获取查询结果的数据
            List<PortalTypeVO> list = resultEntity.getData();

            // 存入模型
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,list);
        }
        return "total";
    }
}
