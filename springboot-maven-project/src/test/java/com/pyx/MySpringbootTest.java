package com.pyx;


import com.pyx.entity.Emp;
import com.pyx.entity.Student;
import com.pyx.mapper.EmpMapper;
import org.junit.jupiter.api.Test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class MySpringbootTest {

    Logger logger = LoggerFactory.getLogger(MySpringbootTest.class);

    @Autowired
    private Student student;

    @Value("${pyx.config.message}")
    private String message;

    @Resource
    private EmpMapper empMapper;

    @Test
    public void test1(){

        logger.info(student.toString());
        System.out.println(student);
        System.out.println(message);
    }

    @Test
    public void test2(){
        List<Emp> empList = empMapper.selectAll();
        for (Emp emp : empList) {
            System.out.println(emp);
        }
    }

    // springboot 整合redis
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    // spring为我们封装好的 一般使用这个
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test3(){

        // 获取用来操作String类型数据的operations对象
        ValueOperations<Object, Object> operations = redisTemplate.opsForValue();

        // 借助operations对象存入数据
        Object key = "good";
        Object value= "morning";
        operations.set(key,value);

        ValueOperations<String, String> operations1 = stringRedisTemplate.opsForValue();
        operations1.set("good","eve");
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        // 在list左处添加元素
        listOperations.leftPush("fruit","apple");
        listOperations.leftPush("fruit","peach");
        listOperations.leftPush("fruit","orange");
    }
}
