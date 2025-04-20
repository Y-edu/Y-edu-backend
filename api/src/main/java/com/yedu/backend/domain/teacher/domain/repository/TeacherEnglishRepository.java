package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherEnglish;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherEnglishRepository extends JpaRepository<TeacherEnglish, Long> {
  Optional<TeacherEnglish> findByTeacher(Teacher teacher);

  void deleteAllByTeacher_TeacherInfo_PhoneNumber(String phoneNumber);
}
