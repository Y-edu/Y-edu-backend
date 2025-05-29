package com.yedu.api.domain.teacher.domain.repository;

import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherMath;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherMathRepository extends JpaRepository<TeacherMath, Long> {
  Optional<TeacherMath> findByTeacher(Teacher teacher);

  Optional<TeacherMath> findByTeacher_TeacherId(long teacherId);

  void deleteAllByTeacher_TeacherInfo_PhoneNumber(String phoneNumber);
}
