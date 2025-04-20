package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherAvailable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherAvailableRepository
    extends JpaRepository<TeacherAvailable, Long>, TeacherAvailableDslRepository {
  List<TeacherAvailable> findAllByTeacher(Teacher teacher);

  void deleteAllByTeacher(Teacher teacher);

  void deleteAllByTeacher_TeacherInfo_PhoneNumber(String phoneNumber);
}
