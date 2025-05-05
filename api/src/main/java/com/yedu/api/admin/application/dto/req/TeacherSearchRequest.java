package com.yedu.api.admin.application.dto.req;

import com.yedu.api.domain.teacher.domain.entity.constant.TeacherGender;
import com.yedu.common.type.ClassType;
import java.util.List;

public record TeacherSearchRequest(
    List<String> districts,
    List<ClassType> subjects,
    List<String> universities,
    List<TeacherGender> genders,
    String search) {}
