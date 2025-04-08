package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherEnglish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherEnglishRepository extends JpaRepository<TeacherEnglish, Long> {
    Optional<TeacherEnglish> findByTeacher(Teacher teacher);

    void deleteAllByTeacher_TeacherInfo_PhoneNumber(String phoneNumber);
}
