package com.yedu.backend.global.bizppurio.application.usecase;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.service.TeacherGetService;
import com.yedu.backend.global.bizppurio.application.dto.req.CommonRequest;
import com.yedu.backend.global.bizppurio.application.mapper.BizppurioMapper;
import com.yedu.backend.global.config.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class BizppurioTeacherMessage {
    private final BizppurioMapper bizppurioMapper;
    private final BizppurioSend bizppurioSend;
    private final RedisRepository redisRepository;
    private static final String COUNSEL = "COUNSEL";

    public void counselStartAndPhotoSubmit(Teacher teacher) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> {
                    CommonRequest request = bizppurioMapper.mapToCounselStart(teacher);
                    redisRepository.setValues(request.refkey(), COUNSEL, Duration.ofMinutes(1));
                    return request;
                })
                .subscribe();
    }

    public void photoSubmit(Teacher teacher) {
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
