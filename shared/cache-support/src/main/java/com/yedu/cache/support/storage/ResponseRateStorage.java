package com.yedu.cache.support.storage;

import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class ResponseRateStorage extends AbstractCacheStorage {

  public ResponseRateStorage(RedisRepository redisRepository) {
    super("teacher-proposed-time:%s", Duration.ofHours(12), redisRepository);
  }
}
