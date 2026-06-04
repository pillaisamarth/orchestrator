package com.sp.pmt_svc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class IdempotentService {
    private final StringRedisTemplate redisTemplate;

    public Boolean isDuplicateAndStore(String orderId){
        String cacheKey = "idempotent:order:" + orderId;

        Boolean isNewClaim = redisTemplate.opsForValue().setIfAbsent(cacheKey, "PROCESSED", 24, TimeUnit.HOURS);
        return Boolean.FALSE.equals(isNewClaim);
    }
}
