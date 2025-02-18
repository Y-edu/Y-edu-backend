package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherDistrictRepository extends JpaRepository<TeacherDistrict, Long> {
    List<TeacherDistrict> findAllByTeacher(Teacher teacher);

    void deleteAllByTeacher_TeacherInfo_PhoneNumber(String phoneNumber);
}
