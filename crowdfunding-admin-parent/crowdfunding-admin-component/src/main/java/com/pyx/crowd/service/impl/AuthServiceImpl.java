package com.pyx.crowd.service.impl;

import com.pyx.crowd.entity.Auth;
import com.pyx.crowd.entity.AuthExample;
import com.pyx.crowd.mapper.AuthMapper;
import com.pyx.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        // 获取roleId的值
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);
        // 删除旧的关联数据
        authMapper.deleteOldRelationship(roleId);
        // 获取authIdList
        List<Integer> authIdList = map.get("authIdArray");
        // 判断authIdList是否有效
        if(authIdList!=null&&authIdList.size()>0){
            authMapper.insertNewRelationship(roleId,authIdList);
        }
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }
}
