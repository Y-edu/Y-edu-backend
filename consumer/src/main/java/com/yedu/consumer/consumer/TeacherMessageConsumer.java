package com.yedu.consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.dto.req.CommonRequest;
import com.yedu.bizppurio.support.application.mapper.BizppurioMapper;
import com.yedu.bizppurio.support.application.usecase.BizppurioApiTemplate;
import com.yedu.common.event.bizppurio.ApplyAgreeEvent;
import com.yedu.common.event.bizppurio.ClassGuideEvent;
import com.yedu.common.event.bizppurio.InviteMatchingChannelInfoEvent;
import com.yedu.common.event.bizppurio.MatchingAcceptCaseInfoEvent;
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
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TeacherMessageConsumer extends AbstractConsumer {

  private final BizppurioMapper mapper;

  public TeacherMessageConsumer(
      ObjectMapper objectMapper,
      BizppurioApiTemplate apiTemplate,
      NotificationRepository notificationRepository,
      BizppurioMapper mapper) {
    super(apiTemplate, notificationRepository, objectMapper);
    this.mapper = mapper;
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
    registerParser(ClassGuideEvent.class, mapper::mapToClassGuide);
  }
}
