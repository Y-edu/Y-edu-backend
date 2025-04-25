package com.yedu.cache.support.storage;

import com.yedu.cache.support.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/***
 * 과외 공지 발송시 사용한 토큰 정보 저장소
 * key : matchingId
 * value : teacherNotifyApplicationFormKeyStorage token
 */
@Component
@RequiredArgsConstructor
public class MatchingIdApplicationNotifyKeyStorage implements TokenStorage<Long> {

  private static final String KEY = "matching-teacher-notify:%s";

  private final RedisRepository redisRepository;

  @Override
  public void store(Long matchingId, String token) {
    redisRepository.setValue(buildKey(matchingId), token);
  }

  @Override
  public String get(Long matchingId) {
    return redisRepository.getValues(buildKey(matchingId)).orElse(null);
  }

  private String buildKey(Long matchingId) {
    return KEY.formatted(matchingId);
  }
}
