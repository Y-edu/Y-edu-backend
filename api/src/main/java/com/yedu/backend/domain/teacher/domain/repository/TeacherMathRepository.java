package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherMath;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherMathRepository extends JpaRepository<TeacherMath, Long> {
  Optional<TeacherMath> findByTeacher(Teacher teacher);

  void deleteAllByTeacher_TeacherInfo_PhoneNumber(String phoneNumber);
}
