package com.yedu.consumer.mapper;

import com.yedu.bizppurio.support.config.BizpurrioTemplate;
import com.yedu.bizppurio.support.config.BizpurrioTemplate.Profile;
import com.yedu.common.event.discord.NotificationDeliverySuccessEvent;
import com.yedu.consumer.domain.notification.entity.Notification;
import com.yedu.consumer.domain.notification.type.ReceiverType;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificationMapper {

  public static NotificationDeliverySuccessEvent map(Notification notification) {
    BizpurrioTemplate bizpurrioTemplate = BizpurrioTemplate.of(notification.getTemplateCode());
    Profile profile = bizpurrioTemplate.getSenderProfile();
    String templateDescription = bizpurrioTemplate.getDesc();
    String content = notification.getContent();
    LocalDateTime deliveredAt = notification.getDeliveredAt();
    ReceiverType receiverType = notification.getReceiverType();
    String receiverPhoneNumber = notification.getReceiverPhoneNumber();

    return new NotificationDeliverySuccessEvent(
        profile.getDecs(),
        templateDescription,
        receiverType.getDesc(),
        receiverPhoneNumber,
        content,
        deliveredAt
    );
  }
}
