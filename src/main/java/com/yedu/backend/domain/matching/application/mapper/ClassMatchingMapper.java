package com.yedu.backend.domain.matching.application.mapper;

import com.yedu.backend.domain.matching.application.dto.res.ClassMatchingForTeacherResponse;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;

import java.util.List;

public class ClassMatchingMapper {
    public static ClassMatching mapToClassMatching(Teacher teacher, ApplicationForm applicationForm) {
        return ClassMatching.builder()
                .applicationForm(applicationForm)
                .teacher(teacher)
                .build();
    }

    public static ClassMatchingForTeacherResponse mapToApplicationFormToTeacherResponse(ClassMatching classMatching, ApplicationForm applicationForm, List<String> goals) {
        return new ClassMatchingForTeacherResponse(
                applicationForm.getApplicationFormId(),
                applicationForm.getWantedSubject(),
                applicationForm.getAge(),
                applicationForm.getClassCount(),
                applicationForm.getClassTime(),
                applicationForm.getOnline(),
                applicationForm.getDistrict().getDescription(),
                applicationForm.getDong(),
                goals,
                applicationForm.getFavoriteStyle(),
                applicationForm.getWantTime(),
                classMatching.getMatchStatus()
        );
    }
}
