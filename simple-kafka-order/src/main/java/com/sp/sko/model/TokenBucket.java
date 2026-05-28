package com.sp.sko.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("tokenBucket")
public class TokenBucket implements Serializable {

    @Id
    private final String userId;
    private final long capacity;
    private double availableTokens;
    private long lastRefillMillis;
    private final double refillTokenPerMillis;

    public TokenBucket(String userId, long capacity, long refillTokens, long refillPeriodMillis){
        this.userId = userId;
        this.capacity = capacity;
        availableTokens = capacity;
        lastRefillMillis = System.currentTimeMillis();
        refillTokenPerMillis = (double) refillTokens / (double) refillPeriodMillis;
    }

    @PersistenceCreator
    public TokenBucket(String userId, long capacity, double availableTokens, double refillTokenPerMillis){
        this.userId = userId;
        this.capacity = capacity;
        this.availableTokens = availableTokens;
        this.lastRefillMillis = System.currentTimeMillis();
        this.refillTokenPerMillis = refillTokenPerMillis;
    }

    synchronized public boolean tryConsume(int numberTokens){
        addTokens();
        if(availableTokens > numberTokens){
            availableTokens -= numberTokens;
            return true;
        }else{
            return false;
        }
    }

    public void addTokens(){
        long systemCurrentMillis = System.currentTimeMillis();
        long sinceLastRefillMillis = systemCurrentMillis - lastRefillMillis;
        double refill = sinceLastRefillMillis * refillTokenPerMillis;
        availableTokens = Math.min(capacity, availableTokens + refill);
        lastRefillMillis = systemCurrentMillis;
    }
}
