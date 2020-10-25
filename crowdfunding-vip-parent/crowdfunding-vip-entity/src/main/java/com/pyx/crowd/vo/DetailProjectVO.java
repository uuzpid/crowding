package com.pyx.crowd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品详情页面该商品的信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailProjectVO {
    private Integer projectId;
    private String projectName;
    private String projectDesc;
    private Integer followerCount;
    private Integer status;
    private Integer money;
    private String statusText;
    private Integer day;
    private Integer supportMoney;
    private Integer percentage;
    private String deployDate;
    private Integer lastDay;
    private Integer supporterCount;
    private String headerPicturePath;
    private List<String> detailPicturePathList;
    private List<DetailReturnVO> detailReturnVOList;
}
