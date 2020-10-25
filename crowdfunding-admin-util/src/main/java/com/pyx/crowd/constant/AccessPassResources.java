package com.pyx.crowd.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * 放行请求的常量类 只用于避免魔法值出现
 */
public class AccessPassResources {
    public static final Set<String> PASS_RES_SET = new HashSet<>();
    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/to/reg/page");
        PASS_RES_SET.add("/auth/member/to/login/page");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/send/short/message.json");

    }

    public static final Set<String> STATIC_RES_SET = new HashSet<>();

    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }

    /**
     * 判断请求类型是否是请求的静态资源
     * @param servletPath
     * @return
     */
    public static boolean judgeCurrentServletPathWeatherStaticResource(String servletPath){
        if(servletPath==null||servletPath.length()==0){
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        // 按/切分请求路径 这里是特殊的，例如请求是 /aa/bb/cc 切分后数组为["","aa","bb,"cc"] 第一个下标为空字符串
        // 拆分后第一个斜杠左边是空字符串
        String[] split = servletPath.split("/");
        String firstLevelPath = split[1];

        // 判断firstLevelPath是否在静态资源的集合中
        return STATIC_RES_SET.contains(firstLevelPath);
    }
}
