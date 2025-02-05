package com.yedu.backend.admin.application.dto.req;

import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;

import java.util.List;

public record TeacherSearchRequest(
        List<District> districts,
        List<ClassType> subjects,
        List<String> universities,
        List<TeacherGender> genders,
        String search
) {
}
