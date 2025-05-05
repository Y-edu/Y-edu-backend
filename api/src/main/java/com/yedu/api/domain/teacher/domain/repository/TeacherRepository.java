package com.yedu.api.domain.teacher.domain.repository;

import com.yedu.api.domain.teacher.domain.entity.Teacher;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, TeacherDslRepository {
  Optional<Teacher> findByTeacherInfo_NameAndTeacherInfo_PhoneNumber(
      String name, String phoneNumber);

  Optional<Teacher> findByTeacherInfo_PhoneNumber(String phoneNumber);

  void deleteAllByTeacherInfo_PhoneNumber(String phoneNumber);

  Optional<Teacher> findByTeacherInfo_NameAndTeacherInfo_NickName(String name, String nickName);
}
