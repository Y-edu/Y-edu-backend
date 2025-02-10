package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherAvailable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherAvailableRepository extends JpaRepository<TeacherAvailable, Long>, TeacherAvailableDslRepository {
    List<TeacherAvailable> findAllByTeacher(Teacher teacher);
}
