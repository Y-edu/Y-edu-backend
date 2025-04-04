package com.yedu.backend.domain.matching.application.usecase;

import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleConfirmRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleRefuseRequest;
import com.yedu.backend.domain.matching.domain.service.ClassManagementCommandService;
import com.yedu.backend.global.event.publisher.BizppurioEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
public class ClassScheduleMatchingUseCase {

  private final ClassManagementCommandService managementCommandService;

  private final BizppurioEventPublisher bizppurioEventPublisher;

  public Long schedule(ClassScheduleMatchingRequest request) {
    Long classManagementId = managementCommandService.schedule(request);

    //TODO : bizppurioEventPublisher 알림톡 발송 처리
    return classManagementId;
  }

  public void refuse(ClassScheduleRefuseRequest request) {
    managementCommandService.delete(request);

    //TODO : bizppurioEventPublisher 알림톡 발송 처리
  }

  public void confirm(ClassScheduleConfirmRequest request) {
    managementCommandService.confirm(request);

    //TODO : bizppurioEventPublisher 알림톡 발송 처리
  }

}
