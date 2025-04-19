package com.yedu.cache.support.storage;

import com.yedu.cache.support.RedisRepository;
import com.yedu.cache.support.dto.TeacherRecommend;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class TeacherRecommendTokenStorage extends AbstractKeyStorage<TeacherRecommend> {

    public TeacherRecommendTokenStorage(RedisRepository redisRepository) {
        super("teacher-recommend:%s", Duration.ofDays(30), redisRepository);
    }
}
