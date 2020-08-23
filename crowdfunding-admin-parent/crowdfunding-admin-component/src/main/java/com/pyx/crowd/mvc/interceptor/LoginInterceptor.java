package com.pyx.crowd.mvc.interceptor;

import com.pyx.crowd.constant.CrowdConstant;
import com.pyx.crowd.entity.Admin;
import com.pyx.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 通过request对象获取session对象
        HttpSession session = request.getSession();
        // 尝试从session域中获取Admin对象
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
        // 判断admin是否为null
        if(admin==null){
            // 抛出异常
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
        }
        // 如果admin对象不为空 返回true放行

        return true;
    }
}
