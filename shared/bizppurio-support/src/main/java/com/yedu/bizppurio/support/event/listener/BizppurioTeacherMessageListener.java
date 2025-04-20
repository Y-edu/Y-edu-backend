package com.yedu.bizppurio.support.event.listener;

import com.yedu.bizppurio.support.application.usecase.BizppurioTeacherMessage;
import com.yedu.common.event.bizppurio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BizppurioTeacherMessageListener {
  private final BizppurioTeacherMessage bizppurioTeacherMessage;

  @EventListener
  @Async
  public void handlePhotoSubmit(PhotoSubmitEvent event) {
    bizppurioTeacherMessage.photoSubmit(event);
  }

  @TransactionalEventListener
  @Async
  public void handlePhotoHurry(PhotoHurryEvent event) {
    bizppurioTeacherMessage.photoHurry(event);
  }

  @EventListener
  @Async
  public void handleApplyAgree(ApplyAgreeEvent event) {
    bizppurioTeacherMessage.applyAgree(event);
  }

  @EventListener
  @Async
  public void handleInviteMatchingChannel(InviteMatchingChannelInfoEvent event) {
    bizppurioTeacherMessage.matchingChannel(event);
  }

  @TransactionalEventListener
  @Async
  public void handleNotifyClass(NotifyClassInfoEvent event) {
    bizppurioTeacherMessage.notifyClass(event);
  }

  @EventListener
  @Async
  public void handleAcceptCase(MatchingAcceptCaseInfoEvent event) {
    bizppurioTeacherMessage.acceptCase(event);
  }

  @EventListener
  @Async
  public void handleRefuseCase(MatchingRefuseCaseEvent event) {
    bizppurioTeacherMessage.refuseCase(event);
  }

  @EventListener
  @Async
  public void handleRefuseCaseNow(MatchingRefuseCaseNowEvent event) {
    bizppurioTeacherMessage.refuseCaseNow(event);
  }

  @EventListener
  @Async
  public void handleRefuseCaseDistrict(MatchingRefuseCaseDistrictEvent event) {
    bizppurioTeacherMessage.refuseCaseDistrict(event);
  }

  @EventListener
  @Async
  public void handleTeacherExchange(TeacherExchangeEvent event) {
    bizppurioTeacherMessage.teacherExchange(event);
  }

  @EventListener
  @Async
  public void handleTeacherClassRemind(TeacherClassRemindEvent event) {
    bizppurioTeacherMessage.teacherClassRemind(event);
  }

  @EventListener
  @Async
  public void handleMatchingConfirmTeacher(MatchingConfirmTeacherEvent event) {
    bizppurioTeacherMessage.matchingConfirmTeacher(event);
  }
}
