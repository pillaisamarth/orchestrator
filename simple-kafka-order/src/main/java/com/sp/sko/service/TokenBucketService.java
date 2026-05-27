package com.sp.sko.service;

import com.sp.sko.model.TokenBucket;
import com.sp.sko.repository.TokenBucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class TokenBucketService {
    private static final int DEFAULT_BUCKET_CAPACITY = 5;
    private static final long DEFAULT_TOKEN_REFILL_PERIOD_MILLIS = 5000;
    private static final long DEFAULT_NUM_TOKENS_REFILL = 2;
    private final TokenBucketRepository repository;

    public boolean tryConsume(String key, int numberTokens){
        TokenBucket tokenBucket = repository.findById(key).orElseGet(defaultTokenBucketSupplier(key));
        boolean success = tokenBucket.tryConsume(numberTokens);
        repository.save(tokenBucket);
        return success;
    }

    private Supplier<TokenBucket> defaultTokenBucketSupplier(String key){
        return () -> new TokenBucket(key, DEFAULT_BUCKET_CAPACITY, DEFAULT_NUM_TOKENS_REFILL, DEFAULT_TOKEN_REFILL_PERIOD_MILLIS);
    }
}
