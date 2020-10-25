package com.pyx.crowd.message;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class ShortMessageTest {

    public static void main(String[] args) {

        String host = "https://aliapi.market.alicloudapi.com";

        String path = "/smsApi/verifyCode/send";

        String method = "POST";

        String appcode = "75d21d877e6740e4a1b6ca3fd843c2b7";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        // 封装其他参数
        Map<String, String> querys = new HashMap<String, String>();

        // 收短信的手机号
        querys.put("phone", "18043016390");

        // 模板编号
        querys.put("templateId", "540");

        // 变量值 验证码
        querys.put("variables", "1234");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
