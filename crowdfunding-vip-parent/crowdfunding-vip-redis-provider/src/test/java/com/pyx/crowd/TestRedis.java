package com.pyx.crowd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test1(){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("apple","red");

    }

    @Test
    public void test2(){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("banana","orange",5000, TimeUnit.SECONDS);
    }

}
