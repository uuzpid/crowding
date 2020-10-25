package com.pyx.crowd.service.api;

import com.pyx.crowd.vo.DetailProjectVO;
import com.pyx.crowd.vo.PortalProjectVO;
import com.pyx.crowd.vo.PortalTypeVO;
import com.pyx.crowd.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberId);

    List<PortalTypeVO> getPortalTypeVO();

    DetailProjectVO getDetailProjectVO(Integer id);
}
