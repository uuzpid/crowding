package com.pyx.crowd.util;

import com.pyx.crowd.constant.CrowdConstant;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CrowdUtil {
    /**
     * 请求格式大概如下。区别在accept
     * Accept: application/json, text/javascript, *;q=0.01
     *Accept-Encoding:gzip,deflate,br
     *Accept-Language:zh-CN,zh;q=0.9
     *Cache-Control:no-cache
     *Connection:keep-alive
     *Content-Length:229
     *Content-Type:application/json;charset=UTF-8
     *Cookie:JSESSIONID=8F3BAA0D65B9AA913A5DB339E645EBE2
     *Host:localhost:8080
     *Origin:http://localhost:8080
     *Pragma:no-cache
     *Referer:http://localhost:8080/
     *Sec-Fetch-Dest:empty
     *Sec-Fetch-Mode:cors
     *Sec-Fetch-Site:same-origin
     *User-Agent:Mozilla/5.0(
     *X-Requested-With:XMLHttpRequest
     */
    /**
     * 判断请求是ajax还是普通请求的工具类
     * true为ajax请求
     */
    public static boolean judgeRequestType(HttpServletRequest request) {
        //获取请求消息头
        String acceptHeader = request.getHeader("Accept");
        String xRequestHeader = request.getHeader("X-Requested-With");
        //判断并返回 true为ajax请求
        return (acceptHeader != null && acceptHeader.contains("application/json"))
                ||
                (xRequestHeader != null && xRequestHeader.contains("XMLHttpRequest"));
    }

    /**
     * md5加密
     * @param source 传入的明文字符串
     * @return 返回加密结果
     */
    public static String md5(String source) {
        // 判断source是否有效
        if (source.length() == 0 || source == null) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        // 获取MessageDigest对象
        String algorithm = "md5";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 获取明文字符串对应的字节数组
            byte[] sourceBytes = source.getBytes();
            // 执行加密 加密后是字节数组。存在数据库不方便，数据库中最好存String
            byte[] digest = messageDigest.digest(sourceBytes);
            // 创建BigInteger对象
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, digest);
            // 按照16进制将bigInteger的值转化为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix);
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
