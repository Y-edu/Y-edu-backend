package com.yedu.consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.config.BizpurrioTemplate;
import com.yedu.common.dto.MessageStatusRequest;
import com.yedu.common.event.bizppurio.BizppurioWebHookEvent;
import com.yedu.common.event.bizppurio.TeacherCompleteTalkChangeNoticeWithGuidelineEvent;
import com.yedu.common.event.discord.NotificationDeliverySuccessEvent;
import com.yedu.consumer.domain.notification.entity.Notification;
import com.yedu.consumer.domain.notification.repository.NotificationRepository;
import com.yedu.consumer.mapper.NotificationMapper;
import com.yedu.rabbitmq.support.Message;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class SystemMessageConsumer implements Consumer<Message> {

  private static final String SUCCESS = "7000";

  private final ObjectMapper objectMapper;

  private final NotificationRepository notificationRepository;

  private final ApplicationEventPublisher applicationEventPublisher;

  public SystemMessageConsumer(
      ObjectMapper objectMapper,
      NotificationRepository notificationRepository,
      ApplicationEventPublisher applicationEventPublisher) {
    this.objectMapper = objectMapper;
    this.notificationRepository = notificationRepository;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Transactional
  @Override
  public void accept(Message message) {
    BizppurioWebHookEvent bizppurioWebHookEvent =
        objectMapper.convertValue(message.data(), BizppurioWebHookEvent.class);
    MessageStatusRequest request = bizppurioWebHookEvent.request();

    updateNotificationStatus(request);
  }

  private void updateNotificationStatus(MessageStatusRequest request) {
    Optional<Notification> notification =
        notificationRepository.findByServerKeyAndClientKey(request.CMSGID(), request.REFKEY());
    if (request.RESULT().equals(SUCCESS)) {
      notification.ifPresent(
          it -> {
            it.successDelivery();
            if (BizpurrioTemplate.YEDU_TUTOR_TEACHER_NOTICE_COMPLETE_TALK_CHANGE.hasSameCode(
                it.getTemplateCode())) {
              applicationEventPublisher.publishEvent(
                  new TeacherCompleteTalkChangeNoticeWithGuidelineEvent(
                      it.getReceiverPhoneNumber()));
            }
            NotificationDeliverySuccessEvent event = NotificationMapper.map(it);
            applicationEventPublisher.publishEvent(event);
          });
      return;
    }
    log.error("delivery failed - webhook response: {}", request);
    notification.ifPresent(Notification::failDelivery);
  }
}
