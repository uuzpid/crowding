package com.pyx.crowd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 首页分类的实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalTypeVO {

    private Integer id;
    private String name;
    private String remark;

    private List<PortalProjectVO> portalProjectVOList;
}
