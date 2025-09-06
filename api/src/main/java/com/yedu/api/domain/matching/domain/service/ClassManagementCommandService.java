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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ClassManagementCommandService {

  private final ClassManagementRepository classManagementRepository;

  private final ClassMatchingGetService classMatchingGetService;

  private final ClassSessionCommandService classSessionCommandService;

  private final PaymentTemplate paymentTemplate;

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
    ClassMatching matching = classManagement.getClassMatching();
    ApplicationForm applicationForm = matching.getApplicationForm();
    String teacherNickname = matching.getTeacher().getTeacherInfo().getNickName();


    classManagement.confirm(
        request.textBook(), request.firstDay().date(), new ClassTime(request.firstDay().start()));

    SendBillRequest sendBillRequest = new SendBillRequest("학부모",
        applicationForm.getParents().getPhoneNumber(),
        """
        {name} 선생님 수업료
        """
        .replace("{name}", teacherNickname),
        """
        ☑️ {name} 선생님 수업 결제 안내
        
        어머님 안녕하세요. 선생님과 수업 진행을 위한 결제 안내 드립니다.   결제 진행 후, 선생님과 전화 상담이 진행되며, 전화상담으로 교재, 수업시간을 확정 후 수업이 진행됩니다.
       
        문의사항이 있으시다면 언제든 Y-Edu 채널을 통해 문의사항 말씀해주세요.   감사합니다!\s
        """
        .replace("{name}", teacherNickname),
        BigDecimal.valueOf(applicationForm.getPay()));

    paymentTemplate.sendBill(sendBillRequest);
    return classManagement;
  }

  public void completeRemind(ClassManagement classManagement) {
    classManagement.completeRemind();
  }

  private ClassManagement findClassManagementWithSchedule(
      ClassScheduleConfirmRequest request, Long id) {
    ClassManagement classManagement = queryById(id);
    classManagement.resetSchedule();

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

    classManagement.resetSchedule();

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
