package com.yedu.cache.support.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class ClassMatchingKeyStorage extends AbstractKeyStorage<Long> {

  public ClassMatchingKeyStorage(RedisRepository redisRepository, ObjectMapper objectMapper) {
    super("class-matching-id:%s", Duration.ofDays(40), redisRepository, objectMapper, Long.class);
  }
}
