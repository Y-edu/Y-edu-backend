package com.yedu.backend.global.event.listener;

import com.yedu.backend.bizppurio.application.usecase.BizppurioTeacherMessage;
import com.yedu.backend.global.event.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BizppurioTeacherMessageListener {
    private final BizppurioTeacherMessage bizppurioTeacherMessage;

    @EventListener
    @Async
    public void handlePhotoSubmit(PhotoSubmitEvent event) {
        bizppurioTeacherMessage.photoSubmit(event);
    }

    @EventListener
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

    @EventListener
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
}
