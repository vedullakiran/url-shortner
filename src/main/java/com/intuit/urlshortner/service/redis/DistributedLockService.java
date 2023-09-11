package com.intuit.urlshortner.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class DistributedLockService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean acquireLock(String lockKey) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 10, TimeUnit.SECONDS));
    }

    public boolean releaseLock(String lockKey) {
        return Boolean.TRUE.equals(redisTemplate.delete(lockKey));
    }
}

