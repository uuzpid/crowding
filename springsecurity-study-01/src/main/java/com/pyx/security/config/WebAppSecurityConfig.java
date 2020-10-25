package com.pyx.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

// 注意这个类一定要放在自动扫描的包下，否则配置不生效
@Configuration
@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private MyPasswordEncoder myPasswordEncoder;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // 将bCryptPasswordEncoder放入IOC容器。才能注入
    @Bean
    public BCryptPasswordEncoder getbCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        /**
         * .inMemoryAuthentication()在内存中完成账号密码地检查
         *  权限或者角色必须要指定一个
         */
        /*builder.inMemoryAuthentication()
                .withUser("tom")
                .password("123123")
                .roles("ADMIN", "学徒")
                .and()
                .withUser("jerry")
                .password("123123")
                .authorities("UPDATE", "内门弟子")
        ;*/
        // 装配userDetailService对象
        builder.userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * 请求授权
     *
     * @param security
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity security) throws Exception {

        // 将记住我 产生的cookie存入数据库
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);

        // security.authorizeRequests() 表示给请求授权
        // .antMatchers("/index.jsp") 指定页面授权
        // .permitAll(); 表示无条件访问 放行
        // 之后的内容表示其他的请求都需要登录后才可以访问
        // .formLogin()使用springsecurity自带的表单形式登录
        // .loginPage("/index.jsp");指定登录的页面 同时会影响到退出，失败的地址
        // .loginProcessingUrl("/do/login.html")指定提交登录表单的地址
        security.authorizeRequests()
                .antMatchers("/index.jsp")
                .permitAll()
                .antMatchers("/layui/**")
                .permitAll()
                .antMatchers("/level1/**")// 针对/level1/这个路径设置访问要求
                .hasRole("学徒")// 要求必须包含学徒这个角色
                .antMatchers("/level2/**")// 针对/level2/这个路径设置访问要求
                .hasAuthority("内门弟子")// 要求必须包含内门弟子这个权限
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/index.jsp")
                .loginProcessingUrl("/do/login.html")
                //.permitAll()
                .usernameParameter("loginAcct") //指定登录账号的请求参数名
                .passwordParameter("userPswd") //指定登录密码的请求参数名
                .defaultSuccessUrl("/main.html")//登录成功后前往的页面
                /*.and()
                .csrf()
                .disable() //禁用CSRF*/
                .and()
                .logout()// 开启退出功能
                .logoutUrl("/do/logout.html")// 指定退出请求的URL地址
                .logoutSuccessUrl("/index.jsp")// 成功退出后前往的地址
                .and()
                .exceptionHandling()// 指定异常处理器
                //.accessDeniedPage("/to/no/auth/page.html")// 访问被拒绝时前往的页面
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse,
                                       AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("message","抱歉，您无法访问这个资源");
                        httpServletRequest.getRequestDispatcher("/WEB-INF/views/no_auth.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })
                .and()
                .rememberMe()
                .tokenRepository(tokenRepository)
        ;

    }
}
