package com.yedu.discord.support.listener;

import com.yedu.common.event.bizppurio.NotifyClassInfoEvent;
import com.yedu.common.event.bizppurio.RecommendTeacherEvent;
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
  public void handleAlarmTalkErrorMessage(AlarmTalkErrorMessageEvent event) {
    discordWebhookUseCase.sendAlarmTalkTokenError(event);
  }

  @EventListener
  @Async
  public void handleAlarmTalkErrorInfo(AlarmTalkErrorInfoEvent event) {
    discordWebhookUseCase.sendAlarmTalkError(event);
  }

  @EventListener
  @Async
  public void handleAlarmTalkErrorWithFirst(AlarmTalkErrorWithFirstEvent event) {
    discordWebhookUseCase.sendAlarmTalkErrorWithFirst(event);
  }

  // todo 시간조율 스프린트 토큰 발송 테스트용
  @EventListener
  @Async
  public void handleNotifyApplicationToTeacherEvent(NotifyClassInfoEvent event) {
    discordWebhookUseCase.sendNotifyClassInfoEvent(event);
  }

  @EventListener
  @Async
  public void handleRecommendTeacherEvent(RecommendTeacherEvent event) {
    discordWebhookUseCase.sendRecommendTeacherEvent(event);
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
