package com.yedu.cache.support.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractKeyStorage<T> implements KeyStorage<T> {

  private final String keyTemplate;
  private final Duration ttl;
  private final RedisRepository redisRepository;
  private final ObjectMapper objectMapper;
  private final Class<T> type;

  @Override
  public String storeAndGet(T id) {
    UUID uuid = UUID.nameUUIDFromBytes(id.toString().getBytes());
    String key = buildKey(uuid.toString());

    String json = serialize(id);
    redisRepository.setValues(key, json, ttl);

    return uuid.toString();
  }

  @Override
  public T get(String uuid) {
    String key = buildKey(uuid);

    return redisRepository.getValues(key).map(this::deserialize).orElse(null);
  }

  @Override
  public T getAndExpire(String uuid, Consumer<T> consumer) {
    String key = buildKey(uuid);
    T value = get(uuid);

    Optional.ofNullable(value)
        .ifPresentOrElse(
            consumer,
            () -> {
              log.error("존재하지 않는 key [{}]", key);
              // todo custom exception 정의 예정
              throw new IllegalArgumentException("존재하지 않는 key: " + key);
            });

    redisRepository.deleteValues(key);
    return value;
  }

  private String buildKey(String uuid) {
    return keyTemplate.formatted(uuid);
  }

  private String serialize(T value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (Exception e) {
      log.error("직렬화 실패: {}", value, e);
      return null;
    }
  }

  private T deserialize(String raw) {
    try {
      return objectMapper.readValue(raw, type);
    } catch (Exception e) {
      log.error("역직렬화 실패: {}", raw, e);
      return null;
    }
  }
}
