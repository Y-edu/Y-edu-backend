package com.yedu.backend.domain.parents.domain.service;

import com.yedu.common.redis.RedisRepository;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TeacherTokenStorage {

    private static final String TEACHER_TOKEN_KEY = "teacher-token:%s";

    private static final Integer TOKEN_EXPIRE_DAYS = 30;

    private final RedisRepository redisRepository;

    public String create(Long teacherId){
        String token = UUID.randomUUID().toString();
        redisRepository.setValues(key(token), teacherId.toString(), Duration.ofDays(TOKEN_EXPIRE_DAYS));

        return token;
    }

    public Optional<Long> get(String token){
        return redisRepository.getValues(key(token))
            .map(Long::parseLong);
    }

    private String key(String token){
        return TEACHER_TOKEN_KEY.formatted(token);
    }

}
