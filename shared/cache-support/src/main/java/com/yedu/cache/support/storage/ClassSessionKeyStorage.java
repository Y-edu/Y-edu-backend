package com.yedu.cache.support.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class ClassSessionKeyStorage extends AbstractKeyStorage<Long> {

  public ClassSessionKeyStorage(RedisRepository redisRepository, ObjectMapper objectMapper) {
    super("class-session:%s", Duration.ofDays(30), redisRepository, objectMapper, Long.class);
  }
}
