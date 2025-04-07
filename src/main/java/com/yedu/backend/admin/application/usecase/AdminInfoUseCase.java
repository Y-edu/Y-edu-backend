package com.yedu.backend.admin.application.usecase;

import com.yedu.backend.admin.application.dto.req.TeacherSearchRequest;
import com.yedu.backend.admin.application.dto.res.*;
import com.yedu.backend.admin.application.dto.res.AllAlarmTalkResponse.AlarmTalkResponse;
import com.yedu.backend.admin.application.mapper.AdminMapper;
import com.yedu.backend.admin.domain.service.AdminGetService;
import com.yedu.backend.domain.matching.domain.entity.ClassManagement;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.backend.domain.matching.domain.service.ClassManagementQueryService;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherDistrict;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.yedu.backend.admin.application.mapper.AdminMapper.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminInfoUseCase {
    private final AdminGetService adminGetService;
  private final ClassManagementQueryService classManagementQueryService;

  public AllApplicationResponse getAllApplication() {
        List<ApplicationForm> applicationForms = adminGetService.allApplication();
        return new AllApplicationResponse(applicationForms.stream()
                .map(applicationForm -> {
                    List<ClassMatching> classMatchings = adminGetService.allMatching(applicationForm.getApplicationFormId());
                    int accept = (int)classMatchings.stream()
                            .filter(ClassMatching::isAcceptStatus)
                            .count();

                  Optional<ClassManagement> classManagement = classMatchings.stream()
                      .filter(ClassMatching::isScheduleConfirm)
                      .findFirst()
                      .map(classMatching -> classManagementQueryService.query(
                          classMatching.getClassMatchingId()).get());

                  return mapToApplicationResponse(applicationForm, accept, classMatchings.size(), classManagement);
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

        List<AlarmTalkResponse> alarmTalkResponses = classMatchings.stream()
                .map(AdminMapper::mapToAlarmTalkResponse)
                .toList();
        int accept = (int)classMatchings.stream()
                .filter(classMatching -> classMatching.getMatchStatus().equals(MatchingStatus.수락) || classMatching.getMatchStatus().equals(MatchingStatus.전송))
                .count();
        int time = (int)Duration.between(applicationForm.getCreatedAt(), LocalDateTime.now())
                .toMinutes();
        return new AllAlarmTalkResponse(accept, classMatchings.size(), time, alarmTalkResponses);
    }

    public ClassDetailsResponse getClassDetails(String applicationFormId) {
        ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
        List<Goal> goals = adminGetService.allGoalByApplicationForm(applicationForm);
        List<String> classGoals = goals.stream()
                .map(Goal::getClassGoal)
                .toList();
        return mapToClassDetailsResponse(applicationForm, classGoals);
    }

    public AllFilteringTeacher searchAllTeacher(String applicationFormId, TeacherSearchRequest request) {
        List<ClassMatching> classMatchings = adminGetService.allMatching(applicationFormId);
        List<Teacher> teachers = adminGetService.allTeacherBySearch(classMatchings, request);
        return  new AllFilteringTeacher(teachers.stream()
                .map(teacher -> {
                    List<TeacherDistrict> teacherDistricts = adminGetService.allDistrictByTeacher(teacher);
                    List<String> districts = teacherDistricts.stream()
                            .map(teacherDistrict -> teacherDistrict.getDistrict().getDescription())
                            .toList();
                    return mapToAllFilteringTeacherResponse(teacher, districts);
                })
                .toList());
    }
}
