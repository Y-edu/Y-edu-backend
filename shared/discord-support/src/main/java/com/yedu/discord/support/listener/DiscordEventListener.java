package com.yedu.discord.support.listener;

import com.yedu.common.event.discord.*;
import com.yedu.discord.support.DiscordWebhookUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DiscordEventListener {
  private final DiscordWebhookUseCase discordWebhookUseCase;

  @EventListener
  @Async
  public void handleScheduleCancel(NotificationDeliverySuccessEvent event) {
    discordWebhookUseCase.sendNotificationDeliverySuccess(event);
  }

  @EventListener
  @Async
  public void handleScheduleCancel(ScheduleCancelEvent event) {
    discordWebhookUseCase.sendScheduleCancel(event);
  }

  @TransactionalEventListener
  @Async
  public void handleTeacherRegister(TeacherRegisterEvent event) {
    discordWebhookUseCase.sendTeacherRegister(event);
  }
}
