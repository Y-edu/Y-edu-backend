package com.yedu.backend.admin.application.usecase;

import static com.yedu.backend.admin.application.mapper.AdminMapper.*;

import com.yedu.backend.admin.application.dto.req.TeacherSearchRequest;
import com.yedu.backend.admin.application.dto.res.AllAlarmTalkResponse;
import com.yedu.backend.admin.application.dto.res.AllAlarmTalkResponse.AlarmTalkResponse;
import com.yedu.backend.admin.application.dto.res.AllApplicationResponse;
import com.yedu.backend.admin.application.dto.res.AllFilteringTeacher;
import com.yedu.backend.admin.application.dto.res.ClassDetailsResponse;
import com.yedu.backend.admin.application.dto.res.CommonParentsResponse;
import com.yedu.backend.admin.application.mapper.AdminMapper;
import com.yedu.backend.admin.domain.service.AdminGetService;
import com.yedu.backend.domain.matching.domain.entity.ClassManagement;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.service.ClassManagementQueryService;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherDistrict;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminInfoUseCase {
  private final AdminGetService adminGetService;
  private final ClassManagementQueryService classManagementQueryService;
  private final ClassMatchingGetService classMatchingGetService;

  public AllApplicationResponse getAllApplication() {
    List<ApplicationForm> applicationForms = adminGetService.allApplication();
    return new AllApplicationResponse(
        applicationForms.stream()
            .map(
                applicationForm -> {
                  List<ClassMatching> classMatchings =
                      adminGetService.allMatching(applicationForm.getApplicationFormId());
                  int accept = acceptCount(classMatchings);

                  Optional<ClassManagement> classManagement =
                      findFirstScheduleConfirmClassManagement(classMatchings);

                  return mapToApplicationResponse(
                      applicationForm, accept, classMatchings.size(), classManagement);
                })
            .toList());
  }

  public CommonParentsResponse getParentsInfo(String applicationFormId) {
    ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
    return mapToCommonParentsResponse(applicationForm);
  }

  public AllAlarmTalkResponse getAlarmTalkInfo(String applicationFormId) {
    ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
    List<ClassMatching> classMatchings = adminGetService.allMatching(applicationFormId);

    List<AlarmTalkResponse> alarmTalkResponses =
        classMatchings.stream().map(AdminMapper::mapToAlarmTalkResponse).toList();
    int accept = acceptCount(classMatchings);
    int time =
        (int) Duration.between(applicationForm.getCreatedAt(), LocalDateTime.now()).toMinutes();
    return new AllAlarmTalkResponse(accept, classMatchings.size(), time, alarmTalkResponses);
  }

  public ClassDetailsResponse getClassDetails(String applicationFormId) {
    ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
    List<Goal> goals = adminGetService.allGoalByApplicationForm(applicationForm);
    List<String> classGoals = goals.stream().map(Goal::getClassGoal).toList();
    Optional<ClassManagement> classManagement =
        findFirstScheduleConfirmClassManagement(
            classMatchingGetService.getByApplicationForm(applicationForm));

    return mapToClassDetailsResponse(applicationForm, classGoals, classManagement);
  }

  private Optional<ClassManagement> findFirstScheduleConfirmClassManagement(
      List<ClassMatching> classMatchingGetService) {
    return classMatchingGetService.stream()
        .filter(ClassMatching::isScheduleConfirm)
        .findFirst()
        .map(
            classMatching ->
                classManagementQueryService.query(classMatching.getClassMatchingId()).get());
  }

  public AllFilteringTeacher searchAllTeacher(
      String applicationFormId, TeacherSearchRequest request) {
    List<ClassMatching> classMatchings = adminGetService.allMatching(applicationFormId);
    List<Teacher> teachers = adminGetService.allTeacherBySearch(classMatchings, request);
    return new AllFilteringTeacher(
        teachers.stream()
            .map(
                teacher -> {
                  List<TeacherDistrict> teacherDistricts =
                      adminGetService.allDistrictByTeacher(teacher);
                  List<String> districts =
                      teacherDistricts.stream()
                          .map(teacherDistrict -> teacherDistrict.getDistrict().getDescription())
                          .toList();
                  return mapToAllFilteringTeacherResponse(teacher, districts);
                })
            .toList());
  }

  private int acceptCount(List<ClassMatching> classMatchings) {
    return (int) classMatchings.stream().filter(ClassMatching::isAcceptStatus).count();
  }
}
