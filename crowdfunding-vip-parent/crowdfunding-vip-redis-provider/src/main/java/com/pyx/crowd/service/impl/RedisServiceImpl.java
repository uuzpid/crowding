package com.pyx.crowd.service.impl;

import com.pyx.crowd.service.RedisService;
import com.pyx.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ResultEntity<String> setRedisKeyValue(String key,String value) {
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @Override
    public ResultEntity<String> setRedisKeyValueWithTimeout(String key, String value, long time, TimeUnit timeUnit) {

        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key,value,time,timeUnit);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @Override
    public ResultEntity<String> getRedisStringValueByKey(String key) {

        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            String result = operations.get(key);
            return ResultEntity.successWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @Override
    public ResultEntity<String> removeRedisKey(String key) {
        try {
            redisTemplate.delete(key);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
