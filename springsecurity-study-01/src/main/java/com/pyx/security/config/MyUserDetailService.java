package com.pyx.security.config;

import com.pyx.security.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 根据表单提交的用户名查询User对象，并装配角色，权限等信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 从数据库查询admin对象
        String sql = "select id,loginacct,userpswd,username,email from t_admin where loginacct=?";
        List<Admin> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Admin.class), username);
        Admin admin = list.get(0);

        // 给admin设置角色权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("Role_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("UPDATE"));

        // 把admin对象和authorities封装到UserDetails
        String password = admin.getUserpswd();
        return new User(username,password,authorities);
    }
}
