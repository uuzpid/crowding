package com.pyx.crowd.handler;

import com.pyx.crowd.service.RedisService;
import com.pyx.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RedisHandler {

    @Autowired
    private RedisService redisService;

    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value){

        return redisService.setRedisKeyValue(key,value);
    }

    // timeout?key=teach&value=yello&time=100&timeUnix=MINUTES
    @RequestMapping("/set/redis/key/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnix") TimeUnit timeUnit){

        return redisService.setRedisKeyValueWithTimeout(key,value,time,timeUnit);
    }

    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> getRedisStringValueByKeyRemote(@RequestParam("key") String key){

        return redisService.getRedisStringValueByKey(key);
    }

    @RequestMapping("/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key){

        return redisService.removeRedisKey(key);
    }
}
