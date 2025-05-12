package com.yedu.cache.support.storage;

import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCacheStorage implements CacheStorage {

  private final String keyTemplate;

  private final Duration ttl;

  private final RedisRepository redisRepository;

  @Override
  public void cache(Long id) {
    String key = buildKey(id);

    redisRepository.setValues(key, LocalDateTime.now().toString(), ttl);
  }

  @Override
  public boolean has(Long id) {
    String key = buildKey(id);

    return redisRepository.getValues(key).isPresent();
  }

  private String buildKey(Long id) {
    return keyTemplate.formatted(id);
  }
}
