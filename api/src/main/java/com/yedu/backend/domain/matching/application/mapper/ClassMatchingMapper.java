package com.yedu.backend.domain.matching.application.mapper;

import com.yedu.backend.domain.matching.application.dto.res.ClassMatchingForTeacherResponse;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.vo.DayTime;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import java.util.List;

public class ClassMatchingMapper {
  public static ClassMatching mapToClassMatching(Teacher teacher, ApplicationForm applicationForm) {
    return ClassMatching.builder().applicationForm(applicationForm).teacher(teacher).build();
  }

  public static ClassMatchingForTeacherResponse mapToApplicationFormToTeacherResponse(
      ClassMatching classMatching,
      ApplicationForm applicationForm,
      List<String> goals,
      List<DayTime> dayTimes) {
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
        dayTimes,
        classMatching.getMatchStatus());
  }
}
