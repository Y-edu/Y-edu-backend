package com.yedu.consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.dto.req.CommonRequest;
import com.yedu.bizppurio.support.application.mapper.BizppurioMapper;
import com.yedu.bizppurio.support.application.usecase.BizppurioApiTemplate;
import com.yedu.cache.support.RedisRepository;
import com.yedu.common.event.bizppurio.*;
import com.yedu.common.event.bizppurio.MatchingParentsEvent.ParentsClassNoticeEvent;
import com.yedu.rabbitmq.support.Message;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ParentMessageConsumer extends AbstractConsumer {

  private static final String NEXT = "NEXT";
  private static final String CLASS_NOTICE = "CLASS_NOTICE|";
  private final BizppurioMapper bizppurioMapper;
  private final BizppurioApiTemplate bizppurioApiTemplate;
  private final RedisRepository redisRepository;

  public ParentMessageConsumer(ObjectMapper objectMapper,
      BizppurioMapper bizppurioMapper, BizppurioApiTemplate bizppurioApiTemplate,
      RedisRepository redisRepository) {
    super(objectMapper);
    this.bizppurioMapper = bizppurioMapper;
    this.bizppurioApiTemplate = bizppurioApiTemplate;
    this.redisRepository = redisRepository;
  }

  @PostConstruct
  void init() {
    handlers.put(NotifyCallingEvent.class, msg -> {
      NotifyCallingEvent event = convert(msg, NotifyCallingEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToNotifyCalling(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(RecommendTeacherEvent.class, msg -> {
      RecommendTeacherEvent event = convert(msg, RecommendTeacherEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToRecommendTeacher(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(RecommendGuideEvent.class, msg -> {
      RecommendGuideEvent event = convert(msg, RecommendGuideEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToRecommendGuid(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(MatchingParentsEvent.class, msg -> {
      MatchingParentsEvent event = convert(msg, MatchingParentsEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToParentsExchangePhoneNumber(
          event.parentsExchangeEvent());
      bizppurioApiTemplate.send(commonRequest);
      try {
        String value =
            objectMapper.writeValueAsString(event.parentsClassNoticeEvent());
        redisRepository.setValues(NEXT + commonRequest.refkey(), CLASS_NOTICE + value, Duration.ofSeconds(5l));
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException(e);
      }

    });

    handlers.put(ParentsClassNoticeEvent.class, msg -> {
      ParentsClassNoticeEvent event = convert(msg, ParentsClassNoticeEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToParentsClassNotice(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(ParentsClassInfoEvent.class, msg -> {
      ParentsClassInfoEvent event = convert(msg, ParentsClassInfoEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToParentsClassInfo(event);
      bizppurioApiTemplate.send(commonRequest);
    });

    handlers.put(PayNotificationEvent.class, msg -> {
      PayNotificationEvent event = convert(msg, PayNotificationEvent.class);
      CommonRequest commonRequest = bizppurioMapper.mapToPayNotification(event);
      bizppurioApiTemplate.send(commonRequest);
    });
  }

  @Override
  void afterConsume(Message message) {

  }
}
