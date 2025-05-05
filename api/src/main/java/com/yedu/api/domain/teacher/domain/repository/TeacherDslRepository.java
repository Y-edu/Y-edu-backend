package com.yedu.api.domain.teacher.domain.repository;

import com.yedu.api.admin.application.dto.req.TeacherSearchRequest;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherAvailable;
import java.util.List;
import java.util.Map;

public interface TeacherDslRepository {
  Map<Teacher, List<TeacherAvailable>> findAllMatchingApplicationForm(
      ApplicationForm applicationForm);

  List<Teacher> findAllSearchTeacher(
      List<ClassMatching> classMatchings, TeacherSearchRequest request);

  List<Teacher> getRemindTeacher();

  List<Teacher> getEmptyAvailableTimeTeacher();
}
