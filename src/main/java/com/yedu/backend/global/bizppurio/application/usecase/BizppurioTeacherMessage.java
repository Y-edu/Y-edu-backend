package com.yedu.backend.global.bizppurio.application.usecase;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.global.bizppurio.application.mapper.BizppurioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BizppurioTeacherMessage {
    private final BizppurioMapper bizppurioMapper;
    private final BizppurioSend bizppurioSend;

    public void counselStartAndPhotoSubmit(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToCounselStart(teacher))
                .block();
        photoSubmit(teacher);
    }

    private void photoSubmit(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToApplyPhotoSubmit(teacher)).subscribe();
    }

    public void photoHurry(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToPhotoHurry(teacher)).subscribe();
    }

    public void applyAgree(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToApplyAgree(teacher)).subscribe();
    }

    public void matchingChannel(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToMatchingChannel(teacher)).subscribe();
    }

    public void applyChannel(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToApplyChannel(teacher)).subscribe();
    }

    public void notifyClass(ApplicationForm applicationForm, Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToNotifyClass(applicationForm, teacher)).subscribe();
    }

    public void acceptCase(ApplicationForm applicationForm, Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToMatchingAcceptCase(applicationForm, teacher)).subscribe();
    }

    public void refuseCase(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToRefuseCase(teacher)).subscribe();
    }
}
