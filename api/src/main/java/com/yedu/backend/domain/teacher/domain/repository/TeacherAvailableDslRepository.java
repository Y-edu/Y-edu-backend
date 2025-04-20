package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherAvailable;
import java.util.List;

public interface TeacherAvailableDslRepository {
  List<TeacherAvailable> allAvailableByTeacher(Teacher teacher);
}
