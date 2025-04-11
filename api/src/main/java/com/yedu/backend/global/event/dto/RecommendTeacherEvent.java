package com.yedu.backend.global.event.dto;

import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;

public record RecommendTeacherEvent(
        String parentsPhoneNumber,
        String teacherNickName,
        District district,
        ClassType classType,
        long teacherId
) {
}
