package com.pyx.crowd.handler;

import com.pyx.crowd.service.api.ProjectService;
import com.pyx.crowd.util.ResultEntity;
import com.pyx.crowd.vo.DetailProjectVO;
import com.pyx.crowd.vo.PortalTypeVO;
import com.pyx.crowd.vo.ProjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectProviderHandler {

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/get/project/detail/remote/{id}")
    ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("id")Integer id){

        try {
            DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(id);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }

    @RequestMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId){

        try {
            projectService.saveProject(projectVO,memberId);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote(){
        List<PortalTypeVO> portalTypeVO = null;
        try {
            portalTypeVO = projectService.getPortalTypeVO();
            return ResultEntity.successWithData(portalTypeVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }
}
