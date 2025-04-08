package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, TeacherDslRepository {
    Optional<Teacher> findByTeacherInfo_NameAndTeacherInfo_PhoneNumber(String name, String phoneNumber);

    Optional<Teacher> findByTeacherInfo_PhoneNumber(String phoneNumber);

    void deleteAllByTeacherInfo_PhoneNumber(String phoneNumber);

    Optional<Teacher> findByTeacherInfo_NameAndTeacherInfo_NickName(String name, String nickName);
}
