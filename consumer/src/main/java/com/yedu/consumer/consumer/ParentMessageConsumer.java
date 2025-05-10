package com.yedu.consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.dto.req.CommonRequest;
import com.yedu.bizppurio.support.application.mapper.BizppurioMapper;
import com.yedu.bizppurio.support.application.usecase.BizppurioApiTemplate;
import com.yedu.cache.support.RedisRepository;
import com.yedu.common.event.bizppurio.MatchingParentsEvent;
import com.yedu.common.event.bizppurio.MatchingParentsEvent.ParentsClassNoticeEvent;
import com.yedu.common.event.bizppurio.NotifyCallingEvent;
import com.yedu.common.event.bizppurio.ParentsClassInfoEvent;
import com.yedu.common.event.bizppurio.PayNotificationEvent;
import com.yedu.common.event.bizppurio.RecommendGuideEvent;
import com.yedu.common.event.bizppurio.RecommendTeacherEvent;
import com.yedu.consumer.domain.notification.entity.Notification;
import com.yedu.consumer.domain.notification.repository.NotificationRepository;
import com.yedu.consumer.domain.notification.type.PushType;
import com.yedu.consumer.domain.notification.type.ReceiverType;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ParentMessageConsumer extends AbstractConsumer {

  private static final String NEXT = "NEXT";
  private static final String CLASS_NOTICE = "CLASS_NOTICE|";

  private final BizppurioMapper mapper;
  private final RedisRepository redisRepository;

  public ParentMessageConsumer(
      ObjectMapper objectMapper,
      BizppurioMapper mapper,
      BizppurioApiTemplate apiTemplate,
      RedisRepository redisRepository,
      NotificationRepository notificationRepository) {
    super(apiTemplate, notificationRepository, objectMapper);
    this.mapper = mapper;
    this.redisRepository = redisRepository;
  }

  @Override
  public Notification beforeConsume(CommonRequest request) {
    return Notification.builder()
        .receiverType(ReceiverType.PARENT)
        .pushType(PushType.BIZPURRIO_KAKAO_ALARMTALK)
        .receiverPhoneNumber(request.to())
        .content(getContent(request))
        .templateCode(request.content().at().getTemplateCode())
        .clientKey(request.refkey())
        .build();
  }

  @PostConstruct
  void init() {
    registerParser(NotifyCallingEvent.class, mapper::mapToNotifyCalling);
    registerParser(RecommendTeacherEvent.class, mapper::mapToRecommendTeacher);
    registerParser(RecommendGuideEvent.class, mapper::mapToRecommendGuid);
    registerParser(ParentsClassNoticeEvent.class, mapper::mapToParentsClassNotice);
    registerParser(ParentsClassInfoEvent.class, mapper::mapToParentsClassInfo);
    registerParser(PayNotificationEvent.class, mapper::mapToPayNotification);
    parsers.put(
        MatchingParentsEvent.class,
        message -> {
          MatchingParentsEvent event = convert(message, MatchingParentsEvent.class);
          CommonRequest commonRequest =
              mapper.mapToParentsExchangePhoneNumber(event.parentsExchangeEvent());
          try {
            String value = objectMapper.writeValueAsString(event.parentsClassNoticeEvent());
            redisRepository.setValues(
                NEXT + commonRequest.refkey(), CLASS_NOTICE + value, Duration.ofSeconds(10L));
          } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
          }
          return commonRequest;
        });
  }
}
