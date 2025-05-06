package com.yedu.consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.usecase.BizppurioCheckStep;
import com.yedu.common.dto.MessageStatusRequest;
import com.yedu.common.event.bizppurio.BizppurioWebHookEvent;
import com.yedu.consumer.domain.notification.entity.Notification;
import com.yedu.consumer.domain.notification.repository.NotificationRepository;
import com.yedu.rabbitmq.support.Message;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemMessageConsumer implements Consumer<Message> {

  private final BizppurioCheckStep bizppurioCheckStep;

  private final ObjectMapper objectMapper;
  private final NotificationRepository notificationRepository;

  public SystemMessageConsumer(ObjectMapper objectMapper, BizppurioCheckStep bizppurioCheckStep,
      NotificationRepository notificationRepository) {
    this.bizppurioCheckStep = bizppurioCheckStep;
    this.objectMapper = objectMapper;
    this.notificationRepository = notificationRepository;
  }

  @Override
  public void accept(Message message) {
    BizppurioWebHookEvent bizppurioWebHookEvent =
        objectMapper.convertValue(message.data(), BizppurioWebHookEvent.class);
    MessageStatusRequest request = bizppurioWebHookEvent.request();

    notificationRepository.findByServerKeyAndClientKey(request.CMSGID(), request.REFKEY())
        .ifPresent(Notification::delivered);

    log.info("webhook 메시지 처리 : {}", request);
    bizppurioCheckStep.checkByWebHook(request);
  }
}
