package com.pyx.crowd.mvc.config;

import com.google.gson.Gson;
import com.pyx.crowd.constant.CrowdConstant;
import com.pyx.crowd.exception.AccessForbiddenException;
import com.pyx.crowd.exception.LoginAcctAlreadyInUseException;
import com.pyx.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.pyx.crowd.exception.LoginFailedException;
import com.pyx.crowd.util.CrowdUtil;
import com.pyx.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@ControllerAdvice 表示当前类是一个基于注解的异常处理类
//每触发一种异常就会调用一个对应的方法
@ControllerAdvice
public class CrowdExceptionResolver {

    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(LoginAcctAlreadyInUseForUpdateException exception,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) throws IOException {
        String viewName = "system-error";

        return commonResolve(viewName,exception,request,response);
    }
    /**
     * 捕获违反表唯一约束的异常
     */
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
        String viewName = "admin-add";

        return commonResolve(viewName,exception,request,response);
    }

    @ExceptionHandler(value = AccessForbiddenException.class)
    public ModelAndView resolveAccessForbiddenException(AccessForbiddenException exception,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
        String viewName = "admin-login";

        return commonResolve(viewName,exception,request,response);
    }

    /**
     * 捕获抛出登录异常
     */
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        // 登录失败后 希望用户回到登录页面
        String viewName = "admin-login";

        return commonResolve(viewName,exception,request,response);
    }

    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolveMathException(ArithmeticException exception,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        String viewName = "system-error";

        return commonResolve(viewName,exception,request,response);//返回到system-error页面
    }

    //@ExceptionHandler 将一个具体的异常类型和一个方法关联起来
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(NullPointerException exception,//实际捕获到的异常类型
                                                    HttpServletRequest request,//当前请求对象
                                                    HttpServletResponse response) throws Exception {//当前响应对象
        String viewName = "system-error";

        return commonResolve(viewName,exception,request,response);//返回到system-error页面
    }

    private ModelAndView commonResolve(String viewName,
                                       Exception exception,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        boolean judgeResult = CrowdUtil.judgeRequestType(request);

        //如果是ajax请求
        if(judgeResult){
            //创建一个ResultEntity对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());
            //创建Gson对象
            Gson gson = new Gson();
            //将resultEntity对象转化为JSON字符串
            String json = gson.toJson(resultEntity);
            //将JSON字符串作为响应体返回给浏览器
            response.getWriter().write(json);
            //由于上面已经通过原生的response对象返回了响应，所以不提供ModelAndView对象
            return null;
        }
        //如果不是ajax请求，则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        //将Exception对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION,exception);
        //设置对应的视图名称
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}
