package com.yedu.api.domain.matching.application.mapper;

import com.yedu.api.domain.matching.application.dto.res.ClassMatchingForTeacherResponse;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.vo.DayTime;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import java.util.List;

public class ClassMatchingMapper {
  public static ClassMatching mapToClassMatching(Teacher teacher, ApplicationForm applicationForm) {
    return ClassMatching.builder().applicationForm(applicationForm).teacher(teacher).build();
  }

  public static ClassMatchingForTeacherResponse mapToApplicationFormToTeacherResponse(
      ClassMatching classMatching,
      ApplicationForm applicationForm,
      List<String> goals,
      List<DayTime> parentDayTimes,
      List<DayTime> teacherDayTimes) {
    return new ClassMatchingForTeacherResponse(
        applicationForm.getApplicationFormId(),
        applicationForm.getWantedSubject(),
        applicationForm.getAge(),
        applicationForm.getClassCount(),
        applicationForm.getClassTime(),
        (int) (applicationForm.getPay() * (5.0 / 6.0)),
        applicationForm.getOnline(),
        applicationForm.getDistrict().getDescription(),
        applicationForm.getDong(),
        goals,
        applicationForm.getFavoriteStyle(),
        applicationForm.getWantTime(),
        teacherDayTimes,
        classMatching.getMatchStatus());
  }
}
