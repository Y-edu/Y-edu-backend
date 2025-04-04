package com.yedu.backend.global.event.publisher;

import com.yedu.backend.global.event.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BizppurioEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishNotifyCallingEvent(NotifyCallingEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

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
}