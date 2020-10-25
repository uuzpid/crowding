package com.pyx.security.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

//@Component
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {

        try {
            // 1.创建MessageDigest对象进行加密
            String algorithm = "MD5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            // 2.获取rawPassword字节数组
            byte[] input = ((String) rawPassword).getBytes();

            // 3.加密
            byte[] output = messageDigest.digest(input);

            // 4.转换为16进制数对应的字符
            String encoded = new BigInteger(1, output).toString(16).toUpperCase();
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 对明文密码进行加密
        String formEncode = encode(rawPassword);

        // 比较
        return Objects.equals(formEncode,encodedPassword);
    }
}
