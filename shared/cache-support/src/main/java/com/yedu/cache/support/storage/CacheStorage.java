package com.yedu.cache.support.storage;

public interface CacheStorage {

  void cache(Long id);

  boolean has(Long id);
}
