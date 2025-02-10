package com.yedu.backend.domain.matching.domain.repository;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassMatchingRepository extends JpaRepository<ClassMatching, Long> , ClassMatchingDslRepository{
    List<ClassMatching> findAllByApplicationForm(ApplicationForm applicationForm);
    Optional<ClassMatching> findByApplicationForm_ApplicationFormIdAndTeacher_TeacherIdAndTeacher_TeacherInfo_PhoneNumber(String applicationFormId, long teacherId, String phoneNumber);
}
