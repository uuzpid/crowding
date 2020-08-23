package com.pyx.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pyx.crowd.entity.Role;
import com.pyx.crowd.mapper.RoleMapper;
import com.pyx.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {

        //开启分页功能
        PageHelper.startPage(pageNum,pageSize);

        //执行查询
        List<Role> roles = roleMapper.selectRoleByKeyword(keyword);

        //封装为PageInfo对象
        return new PageInfo<>(roles);
    }
}