package com.yedu.backend.admin.application.dto.req;

import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;

import com.yedu.common.type.ClassType;
import java.util.List;

public record TeacherSearchRequest(
        List<String> districts,
        List<ClassType> subjects,
        List<String> universities,
        List<TeacherGender> genders,
        String search
) {
}
