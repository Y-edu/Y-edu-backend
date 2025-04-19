package com.yedu.cache.support.storage;

import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
public class ClassManagementKeyStorage extends AbstractKeyStorage<Long> {

    public ClassManagementKeyStorage(RedisRepository redisRepository) {
        super("class-management:%s", Duration.ofDays(30), redisRepository);
    }
}
