package com.yedu.backend.domain.teacher.domain.repository;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;

import java.util.List;

public interface TeacherDslRepository {
    List<Teacher> findAllMatchingApplicationForm(ApplicationForm applicationForm);
}
