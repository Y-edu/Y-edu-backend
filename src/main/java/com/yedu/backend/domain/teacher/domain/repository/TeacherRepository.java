package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

}
