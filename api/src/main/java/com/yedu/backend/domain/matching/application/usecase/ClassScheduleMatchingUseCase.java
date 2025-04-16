package com.yedu.backend.domain.matching.application.usecase;

import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleConfirmRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleRefuseRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleRetrieveRequest;
import com.yedu.backend.domain.matching.application.dto.res.ClassScheduleRetrieveResponse;
import com.yedu.backend.domain.matching.domain.entity.ClassManagement;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.service.ClassManagementCommandService;
import com.yedu.backend.domain.matching.domain.service.ClassManagementKeyStorage;
import com.yedu.backend.domain.matching.domain.service.ClassManagementQueryService;
import com.yedu.backend.global.event.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.yedu.backend.global.event.mapper.BizppurioEventMapper.*;
import static com.yedu.backend.global.event.mapper.DiscordEventMapper.*;


@RequiredArgsConstructor
@Component
@Slf4j
public class ClassScheduleMatchingUseCase {

  private final ClassManagementCommandService managementCommandService;

  private final ClassManagementQueryService managementQueryService;

  private final ClassManagementKeyStorage keyStorage;

  private final EventPublisher eventPublisher;

  public String schedule(ClassScheduleMatchingRequest request) {
    ClassManagement classManagement = managementCommandService.schedule(request);
    String key = keyStorage.storeAndGet(classManagement.getClassManagementId());

    eventPublisher.publishMatchingEvent(
            mapToMatchingParentsEvent(classManagement),
            mapToTeacherExchangeEvent(key, classManagement)
    );
    return key;
  }

  public void refuse(ClassScheduleRefuseRequest request) {
    keyStorage.getAndExpire(request.classScheduleManagementId(),
            key -> {
              ClassMatching classMatching = managementCommandService.delete(request, key);
                eventPublisher.publishScheduleCancelEvent(mapToScheduleCancelEvent(classMatching.getTeacher(), classMatching.getApplicationForm(), request.refuseReason()));
            }
    );
  }

  public void confirm(ClassScheduleConfirmRequest request) {
    keyStorage.getAndExpire(request.classScheduleManagementId(), key -> {
              ClassManagement classManagement = managementCommandService.confirm(request, key);
              eventPublisher.publishMatchingConfirmEvent(
                      mapToParentsClassInfoEvent(classManagement),
                      mapToMatchingConfirmTeacherEvent(classManagement)
              );
            }
    );
  }

  public ClassScheduleRetrieveResponse retrieve(ClassScheduleRetrieveRequest request) {
    Long id = keyStorage.get(request.classScheduleManagementId());

    return managementQueryService.query(request, id)
            .map(ClassScheduleRetrieveResponse::of)
            .orElse(ClassScheduleRetrieveResponse.empty());
  }
}
