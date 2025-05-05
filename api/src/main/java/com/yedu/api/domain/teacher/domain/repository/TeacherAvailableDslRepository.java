package com.yedu.api.domain.teacher.domain.repository;

import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherAvailable;
import java.util.List;

public interface TeacherAvailableDslRepository {
  List<TeacherAvailable> allAvailableByTeacher(Teacher teacher);
}
