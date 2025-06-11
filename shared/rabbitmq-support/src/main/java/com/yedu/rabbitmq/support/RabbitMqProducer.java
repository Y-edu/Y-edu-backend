package com.yedu.rabbitmq.support;

import com.yedu.common.event.bizppurio.ApplyAgreeEvent;
import com.yedu.common.event.bizppurio.BizppurioWebHookEvent;
import com.yedu.common.event.bizppurio.ClassGuideEvent;
import com.yedu.common.event.bizppurio.InviteMatchingChannelInfoEvent;
import com.yedu.common.event.bizppurio.MatchingAcceptCaseInfoEvent;
import com.yedu.common.event.bizppurio.MatchingRefuseCaseDistrictEvent;
import com.yedu.common.event.bizppurio.MatchingRefuseCaseEvent;
import com.yedu.common.event.bizppurio.MatchingRefuseCaseNowEvent;
import com.yedu.common.event.bizppurio.NotifyCallingEvent;
import com.yedu.common.event.bizppurio.NotifyClassInfoEvent;
import com.yedu.common.event.bizppurio.ParentsClassInfoEvent;
import com.yedu.common.event.bizppurio.ParentsClassNoticeEvent;
import com.yedu.common.event.bizppurio.ParentsExchangeEvent;
import com.yedu.common.event.bizppurio.PayNotificationEvent;
import com.yedu.common.event.bizppurio.PhotoHurryEvent;
import com.yedu.common.event.bizppurio.PhotoSubmitEvent;
import com.yedu.common.event.bizppurio.RecommendGuideEvent;
import com.yedu.common.event.bizppurio.RecommendTeacherEvent;
import com.yedu.common.event.bizppurio.TeacherAvailableTimeUpdateRequestEvent;
import com.yedu.common.event.bizppurio.TeacherClassRemindEvent;
import com.yedu.common.event.bizppurio.TeacherCompleteTalkChangeNoticeEvent;
import com.yedu.common.event.bizppurio.TeacherCompleteTalkChangeNoticeWithGuidelineEvent;
import com.yedu.common.event.bizppurio.TeacherNotifyClassInfoEvent;
import com.yedu.common.event.bizppurio.TeacherScheduleEvent;
import com.yedu.common.event.bizppurio.TeacherWithNoScheduleCompleteTalkEvent;
import com.yedu.common.event.bizppurio.TeacherWithScheduleCompleteTalkEvent;
import com.yedu.common.event.bizppurio.TeacherWithScheduleCompleteTalkRemindEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RabbitMqProducer {

  private final RabbitTemplate rabbitTemplate;

  private final RabbitMqProperties properties;

  @EventListener
  @Async
  public void handle(PhotoSubmitEvent event) {
    produceTeacherMessage(event);
  }

  @TransactionalEventListener
  @Async
  public void handle(PhotoHurryEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(ApplyAgreeEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(InviteMatchingChannelInfoEvent event) {
    produceTeacherMessage(event);
  }

  @TransactionalEventListener
  @Async
  public void handle(NotifyClassInfoEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(MatchingAcceptCaseInfoEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(MatchingRefuseCaseEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(MatchingRefuseCaseNowEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(MatchingRefuseCaseDistrictEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(TeacherScheduleEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(TeacherNotifyClassInfoEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(TeacherClassRemindEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(ClassGuideEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(TeacherAvailableTimeUpdateRequestEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(TeacherCompleteTalkChangeNoticeEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(TeacherCompleteTalkChangeNoticeWithGuidelineEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(TeacherWithScheduleCompleteTalkEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(TeacherWithNoScheduleCompleteTalkEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(TeacherWithScheduleCompleteTalkRemindEvent event) {
    produceTeacherMessage(event);
  }

  @EventListener
  @Async
  public void handle(NotifyCallingEvent event) {
    produceParentMessage(event);
  }

  @EventListener
  @Async
  public void handle(RecommendTeacherEvent event) {
    produceParentMessage(event);
  }

  @EventListener
  @Async
  public void handle(RecommendGuideEvent event) {
    produceParentMessage(event);
  }

  @EventListener
  @Async
  public void handle(ParentsExchangeEvent event) {
    produceParentMessage(event);
  }

  @EventListener
  @Async
  public void handle(ParentsClassInfoEvent event) {
    produceParentMessage(event);
  }

  @EventListener
  @Async
  public void handle(PayNotificationEvent event) {
    produceParentMessage(event);
  }

  @EventListener
  @Async
  public void handle(ParentsClassNoticeEvent event) {
    produceParentMessage(event);
  }

  @EventListener
  @Async
  public void handle(BizppurioWebHookEvent event) {
    produceSystemMessage(event);
  }

  private <T> void produceParentMessage(T event) {
    rabbitTemplate.convertAndSend(
        properties.exchange(),
        properties.parent().routingKey(),
        new Message(event.getClass(), event));
  }

  private <T> void produceTeacherMessage(T event) {
    rabbitTemplate.convertAndSend(
        properties.exchange(),
        properties.teacher().routingKey(),
        new Message(event.getClass(), event));
  }

  private <T> void produceSystemMessage(T event) {
    rabbitTemplate.convertAndSend(
        properties.exchange(),
        properties.system().routingKey(),
        new Message(event.getClass(), event));
  }
}
