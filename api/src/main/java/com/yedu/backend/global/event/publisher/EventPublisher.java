package com.yedu.backend.global.event.publisher;

import com.yedu.common.event.bizppurio.*;
import com.yedu.common.event.discord.ScheduleCancelEvent;
import com.yedu.common.event.discord.TeacherRegisterEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishPhotoSubmitEvent(PhotoSubmitEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishPhotoHurryEvent(PhotoHurryEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishApplyAgreeEvent(ApplyAgreeEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishInviteMatchingChannelInfoEvent(InviteMatchingChannelInfoEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishNotifyClassInfoEvent(NotifyClassInfoEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishMatchingAcceptCaseInfoEvent(MatchingAcceptCaseInfoEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishMatchingRefuseCaseEvent(MatchingRefuseCaseEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishMatchingRefuseCaseNowEvent(MatchingRefuseCaseNowEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishMatchingRefuseCaseDistrictEvent(MatchingRefuseCaseDistrictEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishRecommendTeacherEvent(RecommendTeacherEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishRecommendGuideEvent(RecommendGuideEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishMatchingEvent(
        MatchingParentsEvent parentsEvent, TeacherExchangeEvent teacherEvent) {
        applicationEventPublisher.publishEvent(parentsEvent);
        applicationEventPublisher.publishEvent(teacherEvent);
    }

    public void publishMatchingConfirmEvent(ParentsClassInfoEvent parentsClassInfoEvent, MatchingConfirmTeacherEvent teacherEvent) {
        applicationEventPublisher.publishEvent(parentsClassInfoEvent);
        applicationEventPublisher.publishEvent(teacherEvent);
    }

    public void publishTeacherClassRemindEvent(TeacherClassRemindEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishScheduleCancelEvent(ScheduleCancelEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishTeacherRegisterEvent(TeacherRegisterEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
