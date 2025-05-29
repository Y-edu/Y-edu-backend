package com.yedu.api.domain.teacher.domain.repository;

import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherEnglish;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherEnglishRepository extends JpaRepository<TeacherEnglish, Long> {
  Optional<TeacherEnglish> findByTeacher(Teacher teacher);

  Optional<TeacherEnglish> findByTeacher_TeacherId(long teacherId);

  void deleteAllByTeacher_TeacherInfo_PhoneNumber(String phoneNumber);
}
