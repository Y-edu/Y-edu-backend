package com.yedu.cache.support.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.cache.support.RedisRepository;
import com.yedu.cache.support.dto.TeacherRecommend;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class TeacherRecommendTokenStorage extends AbstractKeyStorage<TeacherRecommend> {

    public TeacherRecommendTokenStorage(RedisRepository redisRepository, ObjectMapper objectMapper) {
        super("teacher-recommend:%s", Duration.ofDays(30), redisRepository, objectMapper, TeacherRecommend.class);
    }
}
