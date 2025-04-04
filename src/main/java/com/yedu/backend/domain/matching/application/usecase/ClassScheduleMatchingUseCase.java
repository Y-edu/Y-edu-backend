package com.yedu.backend.domain.matching.application.usecase;

import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleConfirmRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleRefuseRequest;
import com.yedu.backend.domain.matching.domain.entity.ClassManagement;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.entity.ClassSchedule;
import com.yedu.backend.domain.matching.domain.service.ClassManagementCommandService;
import com.yedu.backend.domain.matching.domain.service.ClassManagementQueryService;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.backend.domain.matching.domain.vo.ClassTime;
import com.yedu.backend.global.event.publisher.BizppurioEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Component
@Slf4j
public class ClassScheduleMatchingUseCase {

  private final ClassMatchingGetService classMatchingGetService;

  private final ClassManagementQueryService managementQueryService;

  private final ClassManagementCommandService managementCommandService;

  private final BizppurioEventPublisher bizppurioEventPublisher;

  public Long schedule(ClassScheduleMatchingRequest request) {
    ClassMatching classMatching = classMatchingGetService.classMatchingByApplicationFormIdAndTeacherId(
        request.applicationFormId(), request.teacherId(), request.phoneNumber());

    classMatching.schedule();
    ClassManagement management = managementCommandService.save(classMatching);

    //TODO : bizppurioEventPublisher 알림톡 발송 처리

    return management.getClassManagementId();
  }

  public void refuse(ClassScheduleRefuseRequest request) {
    ClassManagement classManagement = managementQueryService.queryById(
        request.classScheduleManagementId());

    classManagement.refuse(request.refuseReason());

    managementCommandService.delete(classManagement);

    //TODO : bizppurioEventPublisher 알림톡 발송 처리
  }

  public void confirm(ClassScheduleConfirmRequest request) {
    ClassManagement classManagement = findClassManagementWithSchedule(
        request);

    classManagement.updateManagement(
        request.textBook(),
        request.firstDay().date(),
        new ClassTime(request.firstDay().start(), request.firstDay().classMinute())
    );

    //TODO : bizppurioEventPublisher 알림톡 발송 처리
  }

  private ClassManagement findClassManagementWithSchedule(ClassScheduleConfirmRequest request) {
    ClassManagement classManagement = managementQueryService.queryById(
        request.classScheduleManagementId());

    request.schedules().stream().map(schedule-> ClassSchedule.builder()
        .classManagement(classManagement)
        .day(schedule.day())
        .classTime(new ClassTime(schedule.start(), schedule.classMinute()))
        .build())
        .forEach(classManagement::addSchedule);

    return classManagement;
  }
}
