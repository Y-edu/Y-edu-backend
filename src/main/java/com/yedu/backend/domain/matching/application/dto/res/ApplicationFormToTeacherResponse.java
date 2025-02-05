package com.yedu.backend.domain.matching.application.dto.res;

import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.parents.domain.entity.constant.Online;

import java.util.List;

public record ApplicationFormToTeacherResponse(
        String applicationFormId,
        ClassType classType,
        String age,
        String classCount,
        String classTime,
        Online online,
        String district,
        String dong,
        List<String> goals,
        String favoriteStyle,
        String favoriteTime
) {
}
