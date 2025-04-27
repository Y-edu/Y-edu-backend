package com.yedu.cache.support.storage;

import java.util.function.Consumer;

public interface KeyStorage<T> {

  String storeAndGet(T id);

  T get(String key);

  T getAndExpire(String key, Consumer<T> consumer);
}
