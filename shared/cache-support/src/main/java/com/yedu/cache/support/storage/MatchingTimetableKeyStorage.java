package com.yedu.cache.support.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.cache.support.RedisRepository;
import java.time.Duration;

import com.yedu.cache.support.dto.MatchingTimeTableDto;
import org.springframework.stereotype.Component;

@Component
public class MatchingTimetableKeyStorage extends AbstractKeyStorage<MatchingTimeTableDto> {

    public MatchingTimetableKeyStorage(RedisRepository redisRepository, ObjectMapper objectMapper) {
        super("class-matching:%s", Duration.ofDays(30), redisRepository, objectMapper, MatchingTimeTableDto.class);
    }
}
