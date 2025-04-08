package com.yedu.backend.global.event.publisher;

import com.yedu.backend.global.event.dto.ApplyAgreeEvent;
import com.yedu.backend.global.event.dto.InviteMatchingChannelInfoEvent;
import com.yedu.backend.global.event.dto.MatchingAcceptCaseInfoEvent;
import com.yedu.backend.global.event.dto.MatchingConfirmTeacherEvent;
import com.yedu.backend.global.event.dto.MatchingParentsEvent;
import com.yedu.backend.global.event.dto.MatchingRefuseCaseDistrictEvent;
import com.yedu.backend.global.event.dto.MatchingRefuseCaseEvent;
import com.yedu.backend.global.event.dto.MatchingRefuseCaseNowEvent;
import com.yedu.backend.global.event.dto.NotifyClassInfoEvent;
import com.yedu.backend.global.event.dto.ParentsClassInfoEvent;
import com.yedu.backend.global.event.dto.PhotoHurryEvent;
import com.yedu.backend.global.event.dto.PhotoSubmitEvent;
import com.yedu.backend.global.event.dto.RecommendGuideEvent;
import com.yedu.backend.global.event.dto.RecommendTeacherEvent;
import com.yedu.backend.global.event.dto.TeacherClassRemindEvent;
import com.yedu.backend.global.event.dto.TeacherExchangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BizppurioEventPublisher {
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
}
