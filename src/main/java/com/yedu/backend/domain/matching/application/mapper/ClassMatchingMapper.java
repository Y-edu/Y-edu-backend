package com.yedu.backend.domain.matching.application.mapper;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;

public class ClassMatchingMapper {
    public static ClassMatching mapToClassMatching(Teacher teacher, ApplicationForm applicationForm) {
        return ClassMatching.builder()
                .applicationForm(applicationForm)
                .teacher(teacher)
                .build();
    }
}
