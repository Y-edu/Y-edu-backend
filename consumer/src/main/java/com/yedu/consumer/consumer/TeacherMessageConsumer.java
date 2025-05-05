package com.yedu.consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.dto.req.CommonRequest;
import com.yedu.bizppurio.support.application.mapper.BizppurioMapper;
import com.yedu.bizppurio.support.application.usecase.BizppurioApiTemplate;
import com.yedu.cache.support.RedisRepository;
import com.yedu.common.event.bizppurio.*;
import com.yedu.rabbitmq.support.Message;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TeacherMessageConsumer extends AbstractConsumer {
  private static final String NEXT = "NEXT";
  private static final String WRITE_FIN_TALK = "WRITE_FIN_TALK";
  private final BizppurioMapper bizppurioMapper;
  private final BizppurioApiTemplate bizppurioApiTemplate;
  private final RedisRepository redisRepository;

  public TeacherMessageConsumer(ObjectMapper objectMapper,
      BizppurioMapper bizppurioMapper, BizppurioApiTemplate bizppurioApiTemplate,
      RedisRepository redisRepository) {
    super(objectMapper);
    this.bizppurioMapper = bizppurioMapper;
    this.bizppurioApiTemplate = bizppurioApiTemplate;
    this.redisRepository = redisRepository;
  }

  @PostConstruct
  void init() {
    handlers.put(PhotoSubmitEvent.class, msg -> {
      PhotoSubmitEvent event = convert(msg, PhotoSubmitEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToApplyPhotoSubmit(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(PhotoHurryEvent.class, msg -> {
      PhotoHurryEvent event = convert(msg, PhotoHurryEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToPhotoHurry(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(ApplyAgreeEvent.class, msg -> {
      ApplyAgreeEvent event = convert(msg, ApplyAgreeEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToApplyAgree(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(InviteMatchingChannelInfoEvent.class, msg -> {
      InviteMatchingChannelInfoEvent event = convert(msg, InviteMatchingChannelInfoEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToMatchingChannel(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(NotifyClassInfoEvent.class, msg -> {
      NotifyClassInfoEvent event = convert(msg, NotifyClassInfoEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToNotifyClass(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(MatchingAcceptCaseInfoEvent.class, msg -> {
      MatchingAcceptCaseInfoEvent event = convert(msg, MatchingAcceptCaseInfoEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToMatchingAcceptCase(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(MatchingRefuseCaseEvent.class, msg -> {
      MatchingRefuseCaseEvent event = convert(msg, MatchingRefuseCaseEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToRefuseCase(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(MatchingRefuseCaseNowEvent.class, msg -> {
      MatchingRefuseCaseNowEvent event = convert(msg, MatchingRefuseCaseNowEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToRefuseCaseNow(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(MatchingRefuseCaseDistrictEvent.class, msg -> {
      MatchingRefuseCaseDistrictEvent event = convert(msg, MatchingRefuseCaseDistrictEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToRefuseCaseDistrict(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(TeacherExchangeEvent.class, msg -> {
      TeacherExchangeEvent event = convert(msg, TeacherExchangeEvent.class);
      CommonRequest notifyClassInfoRequeest = bizppurioMapper.mapToTeacherNotifyClassInfo(event);
      CommonRequest teacherScheduleRequest= bizppurioMapper.mapToTeacherSchedule(event);

      CompletableFuture<Void> notifyClassInfoFuture = CompletableFuture.runAsync(() -> bizppurioApiTemplate.send(notifyClassInfoRequeest));
      CompletableFuture<Void> notifyScheduleFuture = CompletableFuture.runAsync(() -> bizppurioApiTemplate.send(teacherScheduleRequest));

      CompletableFuture.allOf(notifyClassInfoFuture, notifyScheduleFuture).join();
    });

    handlers.put(TeacherClassRemindEvent.class, msg -> {
      TeacherClassRemindEvent event = convert(msg, TeacherClassRemindEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToTeacherClassRemind(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(MatchingConfirmTeacherEvent.class, msg -> {
      MatchingConfirmTeacherEvent event = convert(msg, MatchingConfirmTeacherEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToClassGuide(event.classGuideEvent());
      String refKey = bizppurioApiTemplate.send(commonRequest);

      commonRequest = bizppurioMapper.mapToIntroduceFinishTalk(
          event.introduceFinishTalkEvent());
      try {
        String value = objectMapper.writeValueAsString(event.introduceWriteFinishTalkEvent());
        redisRepository.setValues(NEXT + refKey, WRITE_FIN_TALK + value, Duration.ofSeconds(5l));
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException(e);
      }
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(MatchingConfirmTeacherEvent.IntroduceWriteFinishTalkEvent.class, msg -> {
      MatchingConfirmTeacherEvent.IntroduceWriteFinishTalkEvent event =
          convert(msg, MatchingConfirmTeacherEvent.IntroduceWriteFinishTalkEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToIntroduceWriteFinishTalk(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(TeacherAvailableTimeUpdateRequestEvent.class, msg -> {
      TeacherAvailableTimeUpdateRequestEvent event =
          convert(msg, TeacherAvailableTimeUpdateRequestEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToTeacherAvailableTimeUpdateRequest(event);
      bizppurioApiTemplate.send(commonRequest);
    });
  }

  @Override
  void afterConsume(Message message) {

  }
}
