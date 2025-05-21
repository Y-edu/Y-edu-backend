package com.yedu.cache.support.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class ClassManagementTokenStorage extends AbstractTokenStorage {

  public ClassManagementTokenStorage(RedisRepository redisRepository) {
    super("class-management-token:%s", Duration.ofDays(35), redisRepository);
  }
}
