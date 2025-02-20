package com.yedu.backend.admin.application.dto.res;

import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.parents.domain.entity.constant.Gender;
import com.yedu.backend.domain.parents.domain.entity.constant.Online;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;

import java.util.List;

public record ClassDetailsResponse(
        String classCount,
        String classTime,
        int pay,
        String age,
        String wantTime,
        ClassType wantedSubject,
        Online online,
        Gender favoriteGender,
        District district,
        String dong,
        List<String> goals,
        String teacherStyle
) {
}
