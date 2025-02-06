package com.yedu.backend.global.bizppurio.application.usecase;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.global.bizppurio.application.mapper.BizppurioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BizppurioParentsMessage {
    private final BizppurioMapper bizppurioMapper;
    private final BizppurioSend bizppurioSend;

    public void writeApplicationForm(Parents parents) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToWriteApplicationForm(parents));
    }

    public void notifyCalling(Parents parents) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToNotifyCalling(parents));
    }

    public void beforeCheck(Parents parents) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToBeforeCheck(parents));
    }

    public void recommendTeacher(ApplicationForm applicationForm, List<Teacher> teachers) {
        Flux.fromIterable(teachers)
                .flatMap(teacher -> bizppurioSend.sendMessageWithExceptionHandling(
                        () -> bizppurioMapper.mapToRecommendTeacher(applicationForm, teacher)
                ))
                .then(recommendGuide(applicationForm.getParents()))
                .subscribe();
    }


    private Mono<Void> recommendGuide(Parents parents) {
        return bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToRecommendGuid(parents));
    }
}
