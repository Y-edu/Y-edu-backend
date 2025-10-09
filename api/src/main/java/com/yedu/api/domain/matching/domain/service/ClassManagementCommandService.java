package com.yedu.api.domain.matching.domain.service;

import com.yedu.api.domain.matching.application.dto.req.ClassScheduleConfirmRequest;
import com.yedu.api.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.api.domain.matching.application.dto.req.ClassScheduleRefuseRequest;
import com.yedu.api.domain.matching.application.dto.req.CreateScheduleRequest;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSchedule;
import com.yedu.api.domain.matching.domain.repository.ClassManagementRepository;
import com.yedu.api.domain.matching.domain.vo.ClassTime;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.global.exception.matching.ClassManagementNotFoundException;
import com.yedu.payment.api.PaymentTemplate;
import com.yedu.payment.api.dto.SendBillRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ClassManagementCommandService {

  private final ClassManagementRepository classManagementRepository;

  private final ClassMatchingGetService classMatchingGetService;

  private final ClassSessionCommandService classSessionCommandService;


  public Optional<ClassManagement> findClassManageMent(Teacher teacher) {
    List<ClassMatching> matchings = classSessionCommandService.createSessionOf(teacher, false, null);

    if (matchings.isEmpty()) {
      matchings = classMatchingGetService.getPaused(teacher);
    }
    if (matchings.isEmpty()) {
      return Optional.empty();
    }

    return matchings.stream()
            .map(matching ->
                    classManagementRepository.findByClassMatching_ClassMatchingId(
                            matching.getClassMatchingId()
                    )
            )
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(Optional.empty());
  }


  public ClassManagement schedule(ClassScheduleMatchingRequest request) {
    ClassMatching classMatching = classMatchingGetService.getById(request.classMatchingId());
    classMatching.startSchedule();

    ClassManagement classManagement =
        classManagementRepository
            .findByClassMatching_ClassMatchingId(request.classMatchingId())
            .orElseGet(
                () -> {
                  ClassManagement newClassManagement =
                      ClassManagement.builder().classMatching(classMatching).build();
                  return classManagementRepository.save(newClassManagement);
                });

    return classManagement;
  }

  public ClassMatching delete(ClassScheduleRefuseRequest request, Long id) {
    ClassManagement classManagement = queryById(id);

    classManagement.refuse(request.refuseReason());

    classManagementRepository.delete(classManagement);

    return classManagement.getClassMatching().initializeProxy();
  }

  public ClassManagement confirm(ClassScheduleConfirmRequest request, Long id) {
    ClassManagement classManagement = findClassManagementWithSchedule(request, id);

    classManagement.confirm(
        request.textBook(), request.firstDay().date(), new ClassTime(request.firstDay().start()));

    return classManagement;
  }

  public void completeRemind(ClassManagement classManagement) {
    classManagement.completeRemind();
  }

  private ClassManagement findClassManagementWithSchedule(
      ClassScheduleConfirmRequest request, Long id) {
    ClassManagement classManagement = queryById(id);
    classManagement.resetSchedule(null);

    request.schedules().stream()
        .map(
            schedule ->
                ClassSchedule.builder()
                    .classManagement(classManagement)
                    .day(schedule.day())
                    .classTime(new ClassTime(schedule.start(), schedule.classMinute()))
                    .build())
        .forEach(classManagement::addSchedule);

    return classManagement;
  }

  private ClassManagement queryById(Long id) {
    return classManagementRepository
        .findById(id)
        .orElseThrow(() -> new ClassManagementNotFoundException("일치하는 매칭 관리 정보를 찾을 수 없습니다"));
  }

  public List<ClassMatching> create(CreateScheduleRequest request, ClassMatching classMatching) {
    ClassManagement classManagement =
        classManagementRepository
            .findByClassMatching_ClassMatchingId(classMatching.getClassMatchingId())
            .orElseGet(
                () -> {
                  ClassManagement newClassManagement =
                      ClassManagement.builder().classMatching(classMatching).build();
                  return classManagementRepository.save(newClassManagement);
                });

    classManagement.resetSchedule(request.changeStartDate());

    classSessionCommandService.deleteSession(classManagement, request.changeStartDate());

    request.schedules().stream()
        .map(
            it ->
                ClassSchedule.builder()
                    .classManagement(classManagement)
                    .classTime(new ClassTime(it.start(), it.classMinute()))
                    .day(it.day())
                    .build())
        .forEach(classManagement::addSchedule);

    Teacher teacher = classMatching.getTeacher();

    return classSessionCommandService.createSessionOf(teacher, true, request.changeStartDate());
  }
}
