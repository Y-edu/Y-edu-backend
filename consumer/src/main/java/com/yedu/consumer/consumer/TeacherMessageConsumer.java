package com.yedu.consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.dto.req.CommonRequest;
import com.yedu.bizppurio.support.application.mapper.BizppurioMapper;
import com.yedu.bizppurio.support.application.usecase.BizppurioApiTemplate;
import com.yedu.cache.support.RedisRepository;
import com.yedu.common.event.bizppurio.ApplyAgreeEvent;
import com.yedu.common.event.bizppurio.InviteMatchingChannelInfoEvent;
import com.yedu.common.event.bizppurio.MatchingAcceptCaseInfoEvent;
import com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent;
import com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent.IntroduceWriteFinishTalkEvent;
import com.yedu.common.event.bizppurio.MatchingRefuseCaseDistrictEvent;
import com.yedu.common.event.bizppurio.MatchingRefuseCaseEvent;
import com.yedu.common.event.bizppurio.MatchingRefuseCaseNowEvent;
import com.yedu.common.event.bizppurio.NotifyClassInfoEvent;
import com.yedu.common.event.bizppurio.PhotoHurryEvent;
import com.yedu.common.event.bizppurio.PhotoSubmitEvent;
import com.yedu.common.event.bizppurio.TeacherAvailableTimeUpdateRequestEvent;
import com.yedu.common.event.bizppurio.TeacherClassRemindEvent;
import com.yedu.common.event.bizppurio.TeacherCompleteTalkChangeNoticeEvent;
import com.yedu.common.event.bizppurio.TeacherNotifyClassInfoEvent;
import com.yedu.common.event.bizppurio.TeacherScheduleEvent;
import com.yedu.common.event.bizppurio.TeacherWithNoScheduleCompleteTalkEvent;
import com.yedu.common.event.bizppurio.TeacherWithScheduleCompleteTalkEvent;
import com.yedu.consumer.domain.notification.entity.Notification;
import com.yedu.consumer.domain.notification.repository.NotificationRepository;
import com.yedu.consumer.domain.notification.type.PushType;
import com.yedu.consumer.domain.notification.type.ReceiverType;
import com.yedu.consumer.domain.notification.type.Status;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TeacherMessageConsumer extends AbstractConsumer {

  private static final String NEXT = "NEXT";
  private static final String WRITE_FIN_TALK = "WRITE_FIN_TALK";

  private final BizppurioMapper mapper;
  private final BizppurioApiTemplate bizppurioApiTemplate;
  private final RedisRepository redisRepository;

  public TeacherMessageConsumer(
      ObjectMapper objectMapper,
      BizppurioApiTemplate apiTemplate,
      NotificationRepository notificationRepository,
      BizppurioMapper mapper,
      BizppurioApiTemplate bizppurioApiTemplate,
      RedisRepository redisRepository) {
    super(apiTemplate, notificationRepository, objectMapper);
    this.mapper = mapper;
    this.bizppurioApiTemplate = bizppurioApiTemplate;
    this.redisRepository = redisRepository;
  }

  @Override
  public Notification beforeConsume(CommonRequest request) {
    return Notification.builder()
        .receiverType(ReceiverType.TEACHER)
        .pushType(PushType.BIZPURRIO_KAKAO_ALARMTALK)
        .receiverPhoneNumber(request.to())
        .content(getContent(request))
        .templateCode(request.content().at().getTemplateCode())
        .clientKey(request.refkey())
        .consumedAt(LocalDateTime.now())
        .status(Status.IN_PROGRESS)
        .build();
  }

  @PostConstruct
  void init() {
    registerParser(PhotoSubmitEvent.class, mapper::mapToApplyPhotoSubmit);
    registerParser(PhotoHurryEvent.class, mapper::mapToPhotoHurry);
    registerParser(ApplyAgreeEvent.class, mapper::mapToApplyAgree);
    registerParser(InviteMatchingChannelInfoEvent.class, mapper::mapToMatchingChannel);
    registerParser(NotifyClassInfoEvent.class, mapper::mapToNotifyClass);
    registerParser(MatchingAcceptCaseInfoEvent.class, mapper::mapToMatchingAcceptCase);
    registerParser(MatchingRefuseCaseEvent.class, mapper::mapToRefuseCase);
    registerParser(MatchingRefuseCaseNowEvent.class, mapper::mapToRefuseCaseNow);
    registerParser(MatchingRefuseCaseDistrictEvent.class, mapper::mapToRefuseCaseDistrict);
    registerParser(TeacherClassRemindEvent.class, mapper::mapToTeacherClassRemind);
    registerParser(
        TeacherAvailableTimeUpdateRequestEvent.class,
        mapper::mapToTeacherAvailableTimeUpdateRequest);
    registerParser(
        TeacherCompleteTalkChangeNoticeEvent.class,
        mapper::mapToTeacherCompleteTalkChangeNoticeEvent);
    registerParser(
        TeacherWithScheduleCompleteTalkEvent.class,
        mapper::mapToTeacherWithScheduleCompleteTalkEvent);
    registerParser(
        TeacherWithNoScheduleCompleteTalkEvent.class,
        mapper::mapToTeacherWithNoScheduleCompleteTalkEvent);
    registerParser(TeacherNotifyClassInfoEvent.class, mapper::mapToTeacherNotifyClassInfo);
    registerParser(TeacherScheduleEvent.class, mapper::mapToTeacherSchedule);
    registerParser(IntroduceWriteFinishTalkEvent.class, mapper::mapToIntroduceWriteFinishTalk);

    parsers.put(
        MatchingConfirmTeacherEvent.class,
        message -> {
          MatchingConfirmTeacherEvent event = convert(message, MatchingConfirmTeacherEvent.class);
          CommonRequest classGuideCommonRequest = mapper.mapToClassGuide(event.classGuideEvent());
          bizppurioApiTemplate.send(classGuideCommonRequest);

          CommonRequest introduceFinishTalkCommonRequest =
              mapper.mapToIntroduceFinishTalk(event.introduceFinishTalkEvent());
          try {
            String value = objectMapper.writeValueAsString(event.introduceWriteFinishTalkEvent());
            redisRepository.setValues(
                NEXT + classGuideCommonRequest.refkey(),
                WRITE_FIN_TALK + value,
                Duration.ofSeconds(10L));
          } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
          }
          return introduceFinishTalkCommonRequest;
        });
  }
}
