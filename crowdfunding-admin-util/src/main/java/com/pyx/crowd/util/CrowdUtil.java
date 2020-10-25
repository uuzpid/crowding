package com.pyx.crowd.util;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.pyx.crowd.constant.CrowdConstant;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class CrowdUtil {

    /**
     * 专门负责上传文件到 OSS 服务器的工具方法
     *
     * @param endpoint        OSS 参数
     * @param accessKeyId     OSS 参数
     * @param accessKeySecret OSS 参数
     * @param inputStream     要上传的文件的输入流
     * @param bucketName      OSS 参数
     * @param bucketDomain    OSS 参数
     * @param originalName    要上传的文件的原始文件名
     * @return 包含上传结果以及上传的文件在 OSS 上的访问路径
     */
    public static ResultEntity<String> uploadFileToOss(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            InputStream inputStream,
            String bucketName,
            String bucketDomain,
            String originalName) {

        // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());

        // 生成上传文件在 OSS 服务器上保存时的文件名
        // 原始文件名：beautfulgirl.jpg
        // 生成文件名：wer234234efwer235346457dfswet346235.jpg
        // 使用 UUID 生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");

        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));

        // 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;

        try {
            // 调用 OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName,
                    inputStream);
            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();
            // 根据响应状态码判断请求是否成功
            if (responseMessage == null) {
            // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;
                // 当前方法返回成功
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                // 如果请求没有成功，获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();
                // 当前方法返回失败
                return ResultEntity.failed(" 当 前 响 应 状 态 码 =" + statusCode + " 错 误 消 息 = "+errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 当前方法返回失败
            return ResultEntity.failed(e.getMessage());
        } finally {
            if (ossClient != null) {
                // 关闭 OSSClient。
                ossClient.shutdown();
            }
        }
    }



    /**
     * 使用远程第三方短信接口发短信
     *
     * @param host       短信接口url地址
     * @param path       具体发送短信的请求地址
     * @param method     请求方式
     * @param phoneNum   接收验证码的手机号
     * @param appCode    调用第三方api的appcode
     * @param templateId 模板编号
     * @return 操作成功 返回生成的验证码，否则返回失败信息
     */
    public static ResultEntity<String> sendShortMessage(
            String host,
            String path,
            String method,
            String phoneNum,
            String appCode,
            String templateId
    ) {
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);

        // 封装其他参数
        Map<String, String> querys = new HashMap<String, String>();

        // 收短信的手机号
        querys.put("phone", phoneNum);

        // 模板编号
        querys.put("templateId", templateId);

        // 变量值 验证码
        // 生成验证码
        // Math.random()生成的是小数
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            // for循环生成四个随机数
            int random = (int) (Math.random() * 10);
            builder.append(random);
        }
        String code = builder.toString();
        querys.put("variables", code);
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            if (response.getStatusLine().getStatusCode() == 200) {
                return ResultEntity.successWithData(code);
            }
            return ResultEntity.failed(response.getStatusLine().getReasonPhrase());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

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
     *
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
