package com.pyx.crowd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页分类下各个项目的实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalProjectVO {

    private Integer projectId;
    private String projectName;
    private String headerPicturePath;
    private Integer money;
    private String deployDate;
    private Integer percentage;
    private Integer supporter;
}
