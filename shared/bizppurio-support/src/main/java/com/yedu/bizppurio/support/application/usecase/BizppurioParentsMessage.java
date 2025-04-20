package com.yedu.bizppurio.support.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.mapper.BizppurioMapper;
import com.yedu.cache.support.RedisRepository;
import com.yedu.common.event.bizppurio.*;
import com.yedu.common.event.bizppurio.MatchingParentsEvent.ParentsClassNoticeEvent;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BizppurioParentsMessage {
  private static final String NEXT = "NEXT";
  private static final String CLASS_NOTICE = "CLASS_NOTICE|";
  private final BizppurioMapper bizppurioMapper;
  private final BizppurioSend bizppurioSend;
  private final ObjectMapper objectMapper;
  private final RedisRepository redisRepository;

  public void notifyCalling(NotifyCallingEvent notifyCallingEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToNotifyCalling(notifyCallingEvent));
  }

  public void recommendTeacher(RecommendTeacherEvent recommendTeacherEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToRecommendTeacher(recommendTeacherEvent));
  }

  public void recommendGuide(RecommendGuideEvent parentsPhoneNumber) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToRecommendGuid(parentsPhoneNumber));
  }

  public void matchingParents(MatchingParentsEvent matchingParentsEvent) {
    String refKey =
        bizppurioSend.sendMessageWithExceptionHandling(
            () ->
                bizppurioMapper.mapToParentsExchangePhoneNumber(
                    matchingParentsEvent.parentsExchangeEvent()));
    try {
      String value =
          objectMapper.writeValueAsString(matchingParentsEvent.parentsClassNoticeEvent());
      redisRepository.setValues(NEXT + refKey, CLASS_NOTICE + value, Duration.ofSeconds(5l));
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void parentsClassNotice(ParentsClassNoticeEvent parentsClassNoticeEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToParentsClassNotice(parentsClassNoticeEvent));
  }

  public void parentsClassInfo(ParentsClassInfoEvent parentsClassInfoEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToParentsClassInfo(parentsClassInfoEvent));
  }
}
