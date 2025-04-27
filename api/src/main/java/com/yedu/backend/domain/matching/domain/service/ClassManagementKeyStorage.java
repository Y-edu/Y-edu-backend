package com.yedu.backend.domain.matching.domain.service;

import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClassManagementKeyStorage {

    private static final String KEY = "class-management:%s";

    private static final int CACHE_EXPIRE_DAYS = 30;

    private final RedisRepository redisRepository;

    public String storeAndGet(Long id){
        UUID uuid = UUID.nameUUIDFromBytes(id.toString().getBytes());
        String key = buildKey(uuid.toString());

        redisRepository.setValues(key, id.toString(), Duration.ofDays(CACHE_EXPIRE_DAYS));

        return uuid.toString();
    }

    public Long getAndExpire(String uuid, Consumer<Long> consumer){
        String key = buildKey(uuid);
        Long value = get(uuid);

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

    public Long get(String uuid) {
        String key = buildKey(uuid);

        return redisRepository.getValues(key).map(Long::valueOf).orElse(null);
    }

    private String buildKey(String uuid) {
        return KEY.formatted(uuid);
    }
}
