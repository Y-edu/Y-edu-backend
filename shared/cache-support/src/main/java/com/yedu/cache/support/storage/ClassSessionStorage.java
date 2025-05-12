package com.yedu.cache.support.storage;

import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class ClassSessionStorage extends AbstractCacheStorage {

  public ClassSessionStorage(RedisRepository redisRepository) {
    super("class-session-send:%s", Duration.ofHours(24), redisRepository);
  }
}
