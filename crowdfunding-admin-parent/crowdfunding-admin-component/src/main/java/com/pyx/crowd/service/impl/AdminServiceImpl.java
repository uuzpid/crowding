package com.pyx.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pyx.crowd.constant.CrowdConstant;
import com.pyx.crowd.entity.Admin;
import com.pyx.crowd.entity.AdminExample;
import com.pyx.crowd.exception.LoginAcctAlreadyInUseException;
import com.pyx.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.pyx.crowd.exception.LoginFailedException;
import com.pyx.crowd.mapper.AdminMapper;
import com.pyx.crowd.service.api.AdminService;
import com.pyx.crowd.util.CrowdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
public class AdminServiceImpl implements AdminService {

    /**
     * 更新请求的方法
     * @param admin
     */
    @Override
    public void update(Admin admin) {

        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Autowired
    private AdminMapper adminMapper;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        // 根据登录账号查询admin对象
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> list = adminMapper.selectByExample(adminExample);
        // 判断admin对象是否为空
        if(list==null||list.size()==0){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if(list.size()>1){
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        // 如果admin对象为空则抛出异常
        Admin admin = list.get(0);
        if(admin==null){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 如果admin对象不为空将数据库密码从admin中取出
        String userPswdDB = admin.getUserPswd();
        // 将表单提交的明文密码进行加密
        String md5 = CrowdUtil.md5(userPswd);
        // 对密码进行比较 如果不同抛出异常
        // 这里两个都是变量，因此没有选择用equals比较两个密码是否相等
        if(!Objects.equals(userPswdDB,md5)){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 如果密码一致 则返回Admin对象
        return admin;
    }

    /**
     * 分页查询
     */
    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        // 调用PageHelper的静态方法开启分页功能
        PageHelper.startPage(pageNum,pageSize);
        // 执行查询
        List<Admin> admins = adminMapper.selectAdminByKeyword(keyword);
        // 封装到PageInfo中，便于展示到页面上。
        return new PageInfo<>(admins);
    }

    /**
     * 删除
     * @param adminId
     */
    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    /**
     * 添加数据
     */
    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    /**
     * 保存数据进数据库
     */
    @Override
    public void saveAdmin(Admin admin) {
        /**
         * 明文密码加密
         */
        String userPswd = admin.getUserPswd();
        userPswd = CrowdUtil.md5(userPswd);
        admin.setUserPswd(userPswd);
        /**
         * 生成创建时间
         */
        Date data = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(data);
        admin.setCreateTime(createTime);

        //执行保存 如果插入相同名字的用户，就会报错，因此由异常要catch
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("异常全类名="+e.getClass().getName());
            if(e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }
}
