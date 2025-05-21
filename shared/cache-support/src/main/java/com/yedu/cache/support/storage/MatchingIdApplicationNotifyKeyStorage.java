package com.yedu.cache.support.storage;

import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import org.springframework.stereotype.Component;

/***
 * 과외 공지 발송시 사용한 토큰 정보 저장소
 * key : matchingId
 * value : teacherNotifyApplicationFormKeyStorage token
 */
@Component
public class MatchingIdApplicationNotifyKeyStorage extends AbstractTokenStorage {

  public MatchingIdApplicationNotifyKeyStorage(RedisRepository redisRepository) {
    super("matching-teacher-notify:%s", Duration.ofDays(35), redisRepository);
  }
}
