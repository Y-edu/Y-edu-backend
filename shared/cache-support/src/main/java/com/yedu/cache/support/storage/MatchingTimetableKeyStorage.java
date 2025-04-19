package com.yedu.cache.support.storage;

import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MatchingTimetableKeyStorage extends AbstractKeyStorage<Long> {

    public MatchingTimetableKeyStorage(RedisRepository redisRepository) {
        super("class-matching:%s", Duration.ofDays(30), redisRepository);
    }
}
