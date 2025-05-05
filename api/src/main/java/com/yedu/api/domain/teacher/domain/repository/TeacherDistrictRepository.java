package com.yedu.api.domain.teacher.domain.repository;

import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherDistrict;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherDistrictRepository extends JpaRepository<TeacherDistrict, Long> {
  List<TeacherDistrict> findAllByTeacher(Teacher teacher);

  void deleteAllByTeacher_TeacherInfo_PhoneNumber(String phoneNumber);

  void deleteAllByTeacher(Teacher teacher);
}
