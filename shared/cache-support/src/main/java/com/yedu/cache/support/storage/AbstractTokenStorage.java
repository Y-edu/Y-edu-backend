package com.yedu.cache.support.storage;

import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractTokenStorage implements TokenStorage<Long> {

  private final String key;
  private final Duration ttl;
  private final RedisRepository redisRepository;

  @Override
  public void store(Long id, String token) {
    redisRepository.setValues(buildKey(id), token, ttl);
  }

  @Override
  public String get(Long id) {
    return redisRepository.getValues(buildKey(id)).orElse(null);
  }

  private String buildKey(Long id) {
    return key.formatted(id);
  }
}
