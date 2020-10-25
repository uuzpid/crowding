package com.pyx.crowd.service.impl;

import com.pyx.crowd.mapper.*;
import com.pyx.crowd.po.MemberConfirmInfoPO;
import com.pyx.crowd.po.MemberLaunchInfoPO;
import com.pyx.crowd.po.ProjectPO;
import com.pyx.crowd.po.ReturnPO;
import com.pyx.crowd.service.api.ProjectService;
import com.pyx.crowd.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ReturnPOMapper returnPOMapper;

    @Resource
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Resource
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Resource
    private ProjectPOMapper projectPOMapper;

    @Resource
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Override
    public DetailProjectVO getDetailProjectVO(Integer id) {
        // 查询得到detailProjectVO对象
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(id);

        // 根据status确定detailProjectVO
        Integer status = detailProjectVO.getStatus();
        switch (status){
            case 0 :
                detailProjectVO.setStatusText("审核中");
                break;
            case 1 :
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2 :
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3 :
                detailProjectVO.setStatusText("已关闭");
                break;
            default:
                break;

        }

        // 根据DeployDate算出lastday
        // 2018-10-1
        String deployDate = detailProjectVO.getDeployDate();

        // 获取当前日期
        Date currentDate = new Date();

        // 设定日期格式化格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 将项目中的日期按照其格式 转换成Date类型
            Date deployDay = format.parse(deployDate);
            // 获取当前日期的时间戳
            long currentTimeStamp = currentDate.getTime();
            // 获取项目的发行日期时间戳和设置的持续时间
            long deployDayTime = deployDay.getTime();
            Integer day = detailProjectVO.getDay();

            // 两个时间戳相减获取天数
            long pastDay = (currentTimeStamp-deployDayTime)/1000/60/60/24;

            // 总计众筹天数-已经过去的天数
            Integer lastDay = (int)(day - pastDay);

            detailProjectVO.setLastDay(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return detailProjectVO;
    }

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberId) {
        // 一. 保存projectPO对象
        // 1.创建一个空的projectPO对象
        ProjectPO projectPO = new ProjectPO();

        // 2.复制属性 把projectVO复制到projectPO
        BeanUtils.copyProperties(projectVO,projectPO);
        projectPO.setMemberid(memberId);
        projectPO.setStatus(0);// 0表示即将开始
        // 生成创建时间存入
        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(time);

        // 3.保存projectPO对象
        // 为了能够获取到自增后的主键，需要到ProjectPOMapper.xml中进行相关设置.
        // <insert id="insertSelective" useGeneratedKeys="true" keyProperty="id" parameterType="com.pyx.crowd.po.ProjectPO">
        // useGeneratedKeys="true":使用生成自增主键。keyProperty="id"：使用实体类中的id字段保存这个key
        // insertSelective有选择的保存 无法提供的条就设为null
        projectPOMapper.insertSelective(projectPO);

        // 从projectPO对象中获取自增key
        Integer projectId = projectPO.getId();

        // 二.保存项目的分类关联关系信息
        // 1.从projectVO对象中获取项目的分类typeIdList
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationship(typeIdList,projectId);

        // 三.保存项目的标签关联关系信息
        List<Integer> tagIdList = projectVO.getTagIdList();
        projectPOMapper.insertTagRelationship(tagIdList,projectId);

        // 四.保存项目中详情图片路径信息
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(detailPicturePathList,projectId);

        // 五.保存项目发起人信息
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO,memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        // 六.保存项目回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        List<ReturnPO> returnPOList = new ArrayList<>();
        for (ReturnVO returnVO : returnVOList) {
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO,returnPO);
            returnPOList.add(returnPO);
        }

        returnPOMapper.insertReturnPOBatch(returnPOList,projectId);

        // 七.保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insertConfirm(memberConfirmInfoPO);
    }

    @Override
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }
}
