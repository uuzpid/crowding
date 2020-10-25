package com.pyx.crowd.service;

import com.pyx.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    ResultEntity<String> setRedisKeyValue(String key,String value);

    ResultEntity<String> setRedisKeyValueWithTimeout(String key, String value, long time, TimeUnit timeUnit);

    ResultEntity<String> getRedisStringValueByKey(String key);

    ResultEntity<String> removeRedisKey(String key);
}
