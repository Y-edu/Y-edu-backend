package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.domain.matching.utils.KeyStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchingTimetableKeyStorage {

    private static final String KEY = "class-matching:%s";
    private final KeyStorage keyStorage;

    public String storeAndGet(Long id){
        return keyStorage.storeAndGet(id, KEY);
    }

    public Long getAndExpire(String uuid, Consumer<Long> consumer){
        return keyStorage.getAndExpire(uuid, consumer, KEY);
    }

    public Long get(String uuid) {
        return keyStorage.get(uuid, KEY);
    }
}
