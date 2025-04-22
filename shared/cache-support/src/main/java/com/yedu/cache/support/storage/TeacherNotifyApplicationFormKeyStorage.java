package com.yedu.cache.support.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.cache.support.RedisRepository;
import com.yedu.cache.support.dto.TeacherNotifyApplicationFormDto;
import java.time.Duration;
import org.springframework.stereotype.Component;

/** 선생님에게 과외 공지시 사용할 토큰 저장소 */
@Component
public class TeacherNotifyApplicationFormKeyStorage
    extends AbstractKeyStorage<TeacherNotifyApplicationFormDto> {

  public TeacherNotifyApplicationFormKeyStorage(
      RedisRepository redisRepository, ObjectMapper objectMapper) {
    super(
        "teacher-notify-appform:%s",
        Duration.ofDays(30), redisRepository, objectMapper, TeacherNotifyApplicationFormDto.class);
  }
}
