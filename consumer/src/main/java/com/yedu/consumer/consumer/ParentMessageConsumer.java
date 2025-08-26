package com.yedu.consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.dto.req.CommonRequest;
import com.yedu.bizppurio.support.application.mapper.BizppurioMapper;
import com.yedu.bizppurio.support.application.usecase.BizppurioApiTemplate;
import com.yedu.common.event.bizppurio.NotifyCallingEvent;
import com.yedu.common.event.bizppurio.ParentCompleteTalkNotifyEvent;
import com.yedu.common.event.bizppurio.ParentsClassInfoEvent;
import com.yedu.common.event.bizppurio.ParentsClassNoticeEvent;
import com.yedu.common.event.bizppurio.ParentsExchangeEvent;
import com.yedu.common.event.bizppurio.PayNotificationEvent;
import com.yedu.common.event.bizppurio.RecommendGuideEvent;
import com.yedu.common.event.bizppurio.RecommendTeacherEvent;
import com.yedu.consumer.domain.notification.entity.Notification;
import com.yedu.consumer.domain.notification.repository.NotificationRepository;
import com.yedu.consumer.domain.notification.type.PushType;
import com.yedu.consumer.domain.notification.type.ReceiverType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ParentMessageConsumer extends AbstractConsumer {

  private final BizppurioMapper mapper;

  public ParentMessageConsumer(
      ObjectMapper objectMapper,
      BizppurioMapper mapper,
      BizppurioApiTemplate apiTemplate,
      NotificationRepository notificationRepository) {
    super(apiTemplate, notificationRepository, objectMapper);
    this.mapper = mapper;
  }

  @Override
  public Notification beforeConsume(CommonRequest request) {
    return Notification.builder()
        .receiverType(ReceiverType.PARENT)
        .pushType(PushType.BIZPURRIO_KAKAO_ALARMTALK)
        .receiverPhoneNumber(request.to())
        .content(getContent(request))
        .templateCode(request.content().getContent().getTemplateCode())
        .clientKey(request.refkey())
        .build();
  }

  @PostConstruct
  void init() {
    registerParser(NotifyCallingEvent.class, mapper::mapToNotifyCalling);
    registerParser(RecommendTeacherEvent.class, mapper::mapToRecommendTeacher);
    registerParser(RecommendGuideEvent.class, mapper::mapToRecommendGuid);
    registerParser(ParentsClassNoticeEvent.class, mapper::mapToParentsClassNotice);
    registerParser(ParentsExchangeEvent.class, mapper::mapToParentsExchangePhoneNumber);
    registerParser(ParentsClassInfoEvent.class, mapper::mapToParentsClassInfo);
    registerParser(PayNotificationEvent.class, mapper::mapToPayNotification);
    registerParser(ParentCompleteTalkNotifyEvent.class, mapper::mapToParentCompleteTalkNotify);
  }
}
