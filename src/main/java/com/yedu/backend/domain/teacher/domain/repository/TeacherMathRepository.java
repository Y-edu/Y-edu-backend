package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherMath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherMathRepository extends JpaRepository<TeacherMath, Long> {
    Optional<TeacherMath> findByTeacher(Teacher teacher);
}
