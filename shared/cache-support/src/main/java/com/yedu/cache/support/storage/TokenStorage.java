package com.yedu.cache.support.storage;

public interface TokenStorage<T> {

  void store(T id, String token);

  String get(T id);
}
