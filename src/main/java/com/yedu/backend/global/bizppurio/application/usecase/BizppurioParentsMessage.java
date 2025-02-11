package com.yedu.backend.global.bizppurio.application.usecase;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.global.bizppurio.application.mapper.BizppurioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BizppurioParentsMessage {
    private final BizppurioMapper bizppurioMapper;
    private final BizppurioSend bizppurioSend;

    public void writeApplicationForm(Parents parents) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToWriteApplicationForm(parents)).subscribe();
    }

    public void notifyCalling(Parents parents) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToNotifyCalling(parents)).subscribe();
    }

    public void beforeCheck(Parents parents) {
        bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToBeforeCheck(parents)).subscribe();
    }

    public void recommendTeacher(List<ClassMatching> classMatchings) {
        ApplicationForm applicationForm = classMatchings.get(0).getApplicationForm();
        // 모든 알림톡 전송을 병렬로 실행
        Mono.when(
                        classMatchings.stream()
                                .map(classMatching -> bizppurioSend.sendMessageWithExceptionHandling(
                                        () -> bizppurioMapper.mapToRecommendTeacher(classMatching.getApplicationForm(), classMatching.getTeacher())
                                ))
                                .toList()
                )
                .flatMap(ignore -> recommendGuide(applicationForm.getParents()))
                .subscribe();
    }


    private Mono<Void> recommendGuide(Parents parents) {
        return bizppurioSend.sendMessageWithExceptionHandling(() -> bizppurioMapper.mapToRecommendGuid(parents));
    }
}
