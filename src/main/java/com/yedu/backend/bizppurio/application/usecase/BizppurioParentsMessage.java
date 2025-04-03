package com.yedu.backend.bizppurio.application.usecase;

import com.yedu.backend.bizppurio.application.mapper.BizppurioMapper;
import com.yedu.backend.global.event.dto.RecommendTeacherEvent;
import com.yedu.backend.global.event.dto.NotifyCallingEvent;
import com.yedu.backend.global.event.dto.RecommendGuideEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BizppurioParentsMessage {
    private final BizppurioMapper bizppurioMapper;
    private final BizppurioSend bizppurioSend;

    public void notifyCalling(NotifyCallingEvent notifyCallingEvent) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToNotifyCalling(notifyCallingEvent)).subscribe();
    }

    public void recommendTeacher(RecommendTeacherEvent recommendTeacherEvent) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToRecommendTeacher(recommendTeacherEvent)).subscribe();
    }


    public void recommendGuide(RecommendGuideEvent parentsPhoneNumber) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToRecommendGuid(parentsPhoneNumber)).subscribe();
    }
}
