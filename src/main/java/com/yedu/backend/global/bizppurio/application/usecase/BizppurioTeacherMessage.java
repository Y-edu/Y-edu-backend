package com.yedu.backend.global.bizppurio.application.usecase;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.global.bizppurio.application.mapper.BizppurioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class BizppurioTeacherMessage {
    private final BizppurioMapper bizppurioMapper;
    private final BizppurioSend bizppurioSend;

    public void counselStartAndPhotoSubmit(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToCounselStart(teacher))
                .then(photoSubmit(teacher))
                .subscribe();
    }

    private Mono<Void> photoSubmit(Teacher teacher) {
        return bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToApplyPhotoSubmit(teacher));
    }

    public void photoHurry(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToPhotoHurry(teacher));
    }

    public void applyAgree(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToApplyAgree(teacher));
    }

    public void matchingChannel(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToMatchingChannel(teacher));
    }

    public void applyChannel(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToApplyChannel(teacher));
    }

    public void notifyClass(ApplicationForm applicationForm, Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToNotifyClass(applicationForm, teacher));
    }

    public void acceptCase(ApplicationForm applicationForm, Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToMatchingAcceptCase(applicationForm, teacher));
    }

    public void refuseCase(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToRefuseCase(teacher));
    }
}
