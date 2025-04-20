package com.yedu.bizppurio.support.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.mapper.BizppurioMapper;
import com.yedu.cache.support.RedisRepository;
import com.yedu.common.event.bizppurio.*;
import com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent.IntroduceWriteFinishTalkEvent;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BizppurioTeacherMessage {
  private static final String NEXT = "NEXT";
  private static final String WRITE_FIN_TALK = "WRITE_FIN_TALK|";
  private final BizppurioMapper bizppurioMapper;
  private final BizppurioSend bizppurioSend;
  private final ObjectMapper objectMapper;
  private final RedisRepository redisRepository;

  public void photoSubmit(PhotoSubmitEvent photoSubmitEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToApplyPhotoSubmit(photoSubmitEvent));
  }

  public void photoHurry(PhotoHurryEvent photoHurryEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToPhotoHurry(photoHurryEvent));
  }

  public void applyAgree(ApplyAgreeEvent applyAgreeEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToApplyAgree(applyAgreeEvent));
  }

  public void matchingChannel(InviteMatchingChannelInfoEvent inviteMatchingChannelInfo) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToMatchingChannel(inviteMatchingChannelInfo));
  }

  public void notifyClass(NotifyClassInfoEvent notifyClassInfoEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToNotifyClass(notifyClassInfoEvent));
  }

  public void acceptCase(MatchingAcceptCaseInfoEvent matchingAcceptCaseInfoEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToMatchingAcceptCase(matchingAcceptCaseInfoEvent));
  }

  public void refuseCase(MatchingRefuseCaseEvent matchingRefuseCaseEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToRefuseCase(matchingRefuseCaseEvent));
  }

  public void refuseCaseNow(MatchingRefuseCaseNowEvent matchingRefuseCaseEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToRefuseCaseNow(matchingRefuseCaseEvent));
  }

  public void refuseCaseDistrict(MatchingRefuseCaseDistrictEvent matchingRefuseCaseEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToRefuseCaseDistrict(matchingRefuseCaseEvent));
  }

  public void teacherExchange(TeacherExchangeEvent teacherExchangeEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToTeacherExchangePhoneNumber(teacherExchangeEvent));
  }

  public void teacherClassRemind(TeacherClassRemindEvent teacherExchangeEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToTeacherClassRemind(teacherExchangeEvent));
  }

  public void matchingConfirmTeacher(MatchingConfirmTeacherEvent event) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToClassGuide(event.classGuideEvent()));
    String refKey =
        bizppurioSend.sendMessageWithExceptionHandling(
            () -> bizppurioMapper.mapToIntroduceFinishTalk(event.introduceFinishTalkEvent()));
    try {
      String value = objectMapper.writeValueAsString(event.introduceWriteFinishTalkEvent());
      redisRepository.setValues(NEXT + refKey, WRITE_FIN_TALK + value, Duration.ofSeconds(5l));
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void introduceWriteFinishTalk(
      IntroduceWriteFinishTalkEvent introduceWriteFinishTalkEvent) {
    bizppurioSend.sendMessageWithExceptionHandling(
        () -> bizppurioMapper.mapToIntroduceWriteFinishTalk(introduceWriteFinishTalkEvent));
  }
}
