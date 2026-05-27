package com.sp.sko.repository;

import com.sp.sko.model.TokenBucket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBucketRepository extends CrudRepository<TokenBucket, String> {
}
