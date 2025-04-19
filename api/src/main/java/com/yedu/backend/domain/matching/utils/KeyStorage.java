package com.yedu.backend.domain.matching.utils;

import com.yedu.cache.support.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyStorage {
    private static final int CACHE_EXPIRE_DAYS = 30;

    private final RedisRepository redisRepository;

    public String storeAndGet(Long id, String type){
        UUID uuid = UUID.nameUUIDFromBytes(id.toString().getBytes());
        String key = buildKey(uuid.toString(), type);

        redisRepository.setValues(key, id.toString(), Duration.ofDays(CACHE_EXPIRE_DAYS));

        return uuid.toString();
    }

    public Long getAndExpire(String uuid, Consumer<Long> consumer, String type){
        String key = buildKey(uuid, type);
        Long value = get(uuid, type);

        Optional.ofNullable(value).ifPresentOrElse(foundValue-> {
            try{
                consumer.accept(foundValue);
            }finally {
                redisRepository.deleteValues(key);
            }
        }, ()-> {
            log.error("존재하지 않는 key [%s]", key);
            throw new IllegalArgumentException();
        });

        return value;
    }

    public Long get(String uuid, String type) {
        String key = buildKey(uuid, type);

        return redisRepository.getValues(key).map(Long::valueOf).orElse(null);
    }

    private String buildKey(String uuid, String type) {
        return type.formatted(uuid);
    }
}
