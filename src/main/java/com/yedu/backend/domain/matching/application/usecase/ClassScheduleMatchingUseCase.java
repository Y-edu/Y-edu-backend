package com.yedu.backend.domain.matching.application.usecase;

import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleConfirmRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleRefuseRequest;
import com.yedu.backend.domain.matching.domain.service.ClassManagementCommandService;
import com.yedu.backend.domain.matching.domain.service.ClassManagementKeyStorage;
import com.yedu.backend.global.event.publisher.BizppurioEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
public class ClassScheduleMatchingUseCase {

  private final ClassManagementCommandService managementCommandService;

  private final ClassManagementKeyStorage keyStorage;

  private final BizppurioEventPublisher bizppurioEventPublisher;

  public String schedule(ClassScheduleMatchingRequest request) {
    Long classManagementId = managementCommandService.schedule(request);
    String key = keyStorage.storeAndGet(classManagementId);

    //TODO : bizppurioEventPublisher 알림톡 발송 처리
    return key;
  }

  public void refuse(ClassScheduleRefuseRequest request) {
    Long id = keyStorage.get(request.classScheduleManagementId());
    managementCommandService.delete(request, id);

    //TODO : bizppurioEventPublisher 알림톡 발송 처리
  }

  public void confirm(ClassScheduleConfirmRequest request) {
    Long id = keyStorage.get(request.classScheduleManagementId());
    managementCommandService.confirm(request, id);

    //TODO : bizppurioEventPublisher 알림톡 발송 처리
  }

}
