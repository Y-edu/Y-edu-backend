package com.yedu.backend.admin.application.dto.res;

import com.yedu.common.type.ClassType;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherStatus;

import java.util.List;

public record AllFilteringTeacher(
        List<FilteringTeacher> filteringTeachers
) {
        public record FilteringTeacher (
        long teacherId,
        String nickName,
        List<ClassType> classTypes,
        String name,
        TeacherGender gender,
        TeacherStatus status,
        int accept,
        int total,
        String university,
        String major,
        List<String> districts,
        String video,
        String issue,
        String phoneNumber
        ){}
}
