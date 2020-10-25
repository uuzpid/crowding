package com.pyx.crowd.test;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.pyx.crowd.util.CrowdUtil;
import com.pyx.crowd.util.ResultEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CrowdTest {

    private Logger logger = LoggerFactory.getLogger(CrowdTest.class);

    @Test
    public void Test1(){
        double random = Math.random();
        System.out.println(random);
    }

    @Test
    public void sendMessage() {

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
        querys.put("variables", "123");
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
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            logger.info("code="+statusCode);
            String reasonPhrase = statusLine.getReasonPhrase();
            logger.info("reason="+reasonPhrase);
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            logger.info(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Test
    public void test2() throws FileNotFoundException {
        System.out.println(new File(".").getAbsolutePath());
        FileInputStream inputStream = new FileInputStream("123.jpg");

        ResultEntity<String> resultEntity = CrowdUtil.uploadFileToOss("http://oss-cn-shanghai.aliyuncs.com",
                "LTAI4G7CLbhrfYkQmgpZbsvD",
                "mJ5FcU6cy1YNoGNnyTp0exVR57FjUc",
                inputStream,
                "pyx20201011",
                "http://pyx20201011.oss-cn-shanghai.aliyuncs.com",
                "123.jpg");
        System.out.println(resultEntity);
    }
}
