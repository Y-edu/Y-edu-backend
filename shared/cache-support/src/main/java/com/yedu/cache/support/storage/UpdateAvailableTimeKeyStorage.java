package com.yedu.cache.support.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class UpdateAvailableTimeKeyStorage extends AbstractKeyStorage<Long> {

  public UpdateAvailableTimeKeyStorage(RedisRepository redisRepository, ObjectMapper objectMapper) {
    super(
        "teacher-available-time:%s",
        Duration.ofDays(30), redisRepository, objectMapper, Long.class);
  }
}
