package com.yedu.bizppurio.support.event.listener;

import com.yedu.bizppurio.support.application.usecase.BizppurioParentsMessage;
import com.yedu.common.event.bizppurio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BizppurioParentsMessageListener {
    private final BizppurioParentsMessage bizppurioParentsMessage;

    @EventListener
    @Async
    public void handleNotifyCalling(NotifyCallingEvent event) {
        bizppurioParentsMessage.notifyCalling(event);
    }

    @EventListener
    @Async
    public void handleRecommendTeacher(RecommendTeacherEvent event) {
        // 여기서는 단일 ApplicationFormEvent만 처리하므로 리스트로 감싸서 전달
        bizppurioParentsMessage.recommendTeacher(event);
    }

    @EventListener
    @Async
    public void handleRecommendGuide(RecommendGuideEvent event) {
        bizppurioParentsMessage.recommendGuide(event);
    }

    @EventListener
    @Async
    public void handleMatchingParentsEvent(MatchingParentsEvent event) {
        bizppurioParentsMessage.matchingParents(event);
    }

    @EventListener
    @Async
    public void handleParentsClassInfoEvent(ParentsClassInfoEvent event) {
        bizppurioParentsMessage.parentsClassInfo(event);
    }
}
