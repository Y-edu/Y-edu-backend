package com.yedu.api.admin.application.dto.res;

import com.yedu.api.domain.teacher.domain.entity.constant.TeacherGender;
import com.yedu.api.domain.teacher.domain.entity.constant.TeacherStatus;
import com.yedu.common.type.ClassType;
import java.util.List;

public record AllFilteringTeacher(List<FilteringTeacher> filteringTeachers) {
  public record FilteringTeacher(
      long teacherId,
      String nickName,
      List<ClassType> classTypes,
      String name,
      TeacherGender gender,
      TeacherStatus status,
      int accept,
      int total,
      String university,
      String major,
      List<String> districts,
      String video,
      String issue,
      String phoneNumber) {}
}
