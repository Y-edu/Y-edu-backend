package com.yedu.backend.domain.matching.application.dto.res;

import com.yedu.backend.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.backend.domain.parents.domain.entity.constant.Online;

import com.yedu.common.type.ClassType;
import java.util.List;

public record ClassMatchingForTeacherResponse(
        String applicationFormId,
        ClassType classType,
        String age,
        String classCount,
        String classTime,
        int pay,
        Online online,
        String district,
        String dong,
        List<String> goals,
        String favoriteStyle,
        String favoriteTime,
        MatchingStatus matchStatus
) {
}
