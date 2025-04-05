package com.yedu.backend.admin.domain.service;

import com.yedu.backend.global.config.redis.RedisRepository;
import java.time.Duration;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseRateStorage {

  private static final String RATE_STORAGE_KEY = "teacher-proposed-time:%s";
  private static final int RESPONSE_BEFORE = 12;

  private final RedisRepository redisRepository;

  public void cache(Long teacherId){
    String key = buildKey(teacherId);

    redisRepository.setValues(key, LocalTime.now().toString(), Duration.ofHours(RESPONSE_BEFORE));
  }

  public boolean has(Long teacherId){
    String key = buildKey(teacherId);

    return redisRepository.getValues(key).isPresent();
  }

  private String buildKey(Long teacherId) {
    return RATE_STORAGE_KEY.formatted(teacherId);
  }

}
