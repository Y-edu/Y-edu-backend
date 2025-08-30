package com.yedu.api.admin.application.mapper;

import com.yedu.api.admin.application.dto.res.AllAlarmTalkResponse.AlarmTalkResponse;
import com.yedu.api.admin.application.dto.res.AllApplicationResponse.ApplicationResponse;
import com.yedu.api.admin.application.dto.res.AllFilteringTeacher.FilteringTeacher;
import com.yedu.api.admin.application.dto.res.ClassDetailsResponse;
import com.yedu.api.admin.application.dto.res.CommonParentsResponse;
import com.yedu.api.admin.application.dto.res.ScheduledClass;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.vo.ClassTime;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherClassInfo;
import com.yedu.api.domain.teacher.domain.entity.TeacherInfo;
import com.yedu.common.type.ClassType;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AdminMapper {
  public static ApplicationResponse mapToApplicationResponse(
      ApplicationForm applicationForm,
      int accept,
      int total,
      Optional<ClassManagement> classManagement,
      int payPendingSessionCount,
      Integer totalClassTime,
      Long classMatchingId) {
    Parents parents = applicationForm.getParents();
    String kakaoName = Optional.ofNullable(parents.getKakaoName()).orElse(null);
    List<ScheduledClass> scheduledClasses = getScheduledClasses(classManagement);

    int maxRound =
        classManagement
            .map(ClassManagement::getClassMatching)
            .map(ClassMatching::getApplicationForm)
            .map(ApplicationForm::maxRoundNumber)
            .orElse(1);

    return new ApplicationResponse(
        applicationForm.getApplicationFormId(),
        kakaoName,
        applicationForm.getClassCount(),
        applicationForm.getClassTime(),
        scheduledClasses,
        applicationForm.getPay(),
        applicationForm.getWantedSubject().name(),
        applicationForm.getSource(),
        applicationForm.getCreatedAt().toString(),
        accept,
        total,
        parents.getPhoneNumber(),
        applicationForm.isProceedStatus(),
        payPendingSessionCount,
        maxRound,
        totalClassTime,
        classMatchingId);
  }

  public static CommonParentsResponse mapToCommonParentsResponse(ApplicationForm applicationForm) {
    Parents parents = applicationForm.getParents();
    String kakaoName = Optional.ofNullable(parents.getKakaoName()).orElse(null);
    return new CommonParentsResponse(
        parents.getParentsId(),
        applicationForm.getApplicationFormId(),
        applicationForm.getWantedSubject().name(),
        kakaoName,
        parents.getPhoneNumber());
  }

  public static AlarmTalkResponse mapToAlarmTalkResponse(ClassMatching classMatching) {
    Teacher teacher = classMatching.getTeacher();
    ApplicationForm applicationForm = classMatching.getApplicationForm();
    String refuseReason = Optional.ofNullable(classMatching.getRefuseReason()).orElse(null);
    return new AlarmTalkResponse(
        classMatching.getClassMatchingId(),
        teacher.getTeacherId(),
        applicationForm.getWantedSubject(),
        classMatching.getMatchStatus(),
        teacher.getTeacherInfo().getNickName(),
        teacher.getTeacherInfo().getName(),
        0,
        teacher.getResponseCount(),
        teacher.getTotalRequestCount(),
        refuseReason,
        teacher.getTeacherInfo().getPhoneNumber());
  }

  public static ClassDetailsResponse mapToClassDetailsResponse(
      ApplicationForm applicationForm,
      List<String> goals,
      Optional<ClassManagement> classManagement) {
    List<ScheduledClass> scheduledClasses = getScheduledClasses(classManagement);

    LocalTime firstClassStartTime =
        classManagement
            .flatMap(cm -> Optional.ofNullable(cm.getClassTime()))
            .map(ClassTime::getStart)
            .orElse(null);

    return new ClassDetailsResponse(
        applicationForm.getClassCount(),
        applicationForm.getClassTime(),
        classManagement.map(ClassManagement::getTextbook).orElse(null),
        classManagement.map(ClassManagement::getFirstDay).orElse(null),
        firstClassStartTime,
        scheduledClasses,
        applicationForm.getPay(),
        applicationForm.getAge(),
        applicationForm.getWantTime(),
        applicationForm.getWantedSubject(),
        applicationForm.getOnline(),
        applicationForm.getFavoriteGender(),
        applicationForm.getDistrict(),
        applicationForm.getDong(),
        goals,
        applicationForm.getFavoriteStyle(),
        applicationForm.getSource());
  }

  private static List<ScheduledClass> mapSchedulesToScheduledClasses(ClassManagement management) {
    return management.getSchedules().stream()
        .map(
            schedule ->
                new ScheduledClass(
                    schedule.getDay(),
                    schedule.getClassTime().getStart(),
                    schedule.getClassTime().getClassMinute()))
        .toList();
  }

  private static List<ScheduledClass> getScheduledClasses(
      Optional<ClassManagement> classManagement) {
    return classManagement
        .map(AdminMapper::mapSchedulesToScheduledClasses)
        .orElse(Collections.emptyList());
  }

  public static FilteringTeacher mapToAllFilteringTeacherResponse(
      Teacher teacher, List<String> districts) {
    TeacherInfo teacherInfo = teacher.getTeacherInfo();
    TeacherClassInfo classInfo = teacher.getTeacherClassInfo();
    List<ClassType> classTypes = new ArrayList<>();
    if (classInfo.isEnglishPossible()) classTypes.add(ClassType.영어);
    if (classInfo.isMathPossible()) classTypes.add(ClassType.수학);

    return new FilteringTeacher(
        teacher.getTeacherId(),
        teacherInfo.getNickName(),
        classTypes,
        teacherInfo.getName(),
        teacherInfo.getGender(),
        teacher.getStatus(),
        0,
        0,
        teacher.getTeacherSchoolInfo().getUniversity(),
        teacher.getTeacherSchoolInfo().getMajor(),
        districts,
        teacherInfo.getVideo(),
        teacher.getIssue(),
        teacherInfo.getPhoneNumber());
  }
}
