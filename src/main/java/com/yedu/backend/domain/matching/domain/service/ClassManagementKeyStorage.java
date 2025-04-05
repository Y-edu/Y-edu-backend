package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.global.config.redis.RedisRepository;
import java.time.Duration;
import java.util.UUID;
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

    public Long get(String uuid){
        String key = buildKey(uuid);
        String value = redisRepository.getValues(key).orElseThrow(() -> {
            log.error("존재하지 않은 key 접근 [%s]".formatted(uuid));
            return new IllegalArgumentException();
        });

        return Long.valueOf(value);
    }

    private String buildKey(String uuid) {
        return KEY.formatted(uuid);
    }
}
