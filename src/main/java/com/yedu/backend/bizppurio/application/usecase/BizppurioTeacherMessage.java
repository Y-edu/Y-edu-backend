package com.yedu.backend.bizppurio.application.usecase;

import com.yedu.backend.bizppurio.application.mapper.BizppurioMapper;
import com.yedu.backend.global.event.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BizppurioTeacherMessage {
    private final BizppurioMapper bizppurioMapper;
    private final BizppurioSend bizppurioSend;

    public void photoSubmit(PhotoSubmitEvent photoSubmitEvent) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToApplyPhotoSubmit(photoSubmitEvent)).subscribe();
    }

    public void photoHurry(PhotoHurryEvent photoHurryEvent) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToPhotoHurry(photoHurryEvent)).subscribe();
    }

    public void applyAgree(ApplyAgreeEvent applyAgreeEvent) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToApplyAgree(applyAgreeEvent)).subscribe();
    }

    public void matchingChannel(InviteMatchingChannelInfoEvent inviteMatchingChannelInfo) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToMatchingChannel(inviteMatchingChannelInfo)).subscribe();
    }

    public void notifyClass(NotifyClassInfoEvent notifyClassInfoEvent) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToNotifyClass(notifyClassInfoEvent)).subscribe();
    }

    public void acceptCase(MatchingAcceptCaseInfoEvent matchingAcceptCaseInfoEvent) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToMatchingAcceptCase(matchingAcceptCaseInfoEvent)).subscribe();
    }

    public void refuseCase(MatchingRefuseCaseEvent matchingRefuseCaseEvent) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToRefuseCase(matchingRefuseCaseEvent)).subscribe();
    }

    public void refuseCaseNow(MatchingRefuseCaseNowEvent matchingRefuseCaseEvent) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToRefuseCaseNow(matchingRefuseCaseEvent)).subscribe();
    }

    public void refuseCaseDistrict(MatchingRefuseCaseDistrictEvent matchingRefuseCaseEvent) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToRefuseCaseDistrict(matchingRefuseCaseEvent)).subscribe();
    }
}
