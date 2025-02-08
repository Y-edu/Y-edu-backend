package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, TeacherDslRepository {
    Optional<Teacher> findByTeacherInfo_PhoneNumber(String PhoneNumber);
}
