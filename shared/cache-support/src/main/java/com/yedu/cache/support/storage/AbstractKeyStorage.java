package com.yedu.cache.support.storage;

import com.yedu.cache.support.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractKeyStorage<T> implements KeyStorage<T> {

    private final String keyTemplate;

    private final Duration ttl;

    private final RedisRepository redisRepository;

    @Override
    public String storeAndGet(T id){
        UUID uuid = UUID.nameUUIDFromBytes(id.toString().getBytes());
        String key = buildKey(uuid.toString());

        redisRepository.setValues(key, id.toString(), ttl);

        return uuid.toString();
    }
    @Override
    public T getAndExpire(String uuid, Consumer<T> consumer){
        String key = buildKey(uuid);
        T value = get(uuid);

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
    @Override
    public T get(String uuid) {
        String key = buildKey(uuid);

        return redisRepository.getValues(key).map(value-> (T) value).orElse(null);
    }

    private String buildKey(String uuid) {
        return keyTemplate.formatted(uuid);
    }


}
