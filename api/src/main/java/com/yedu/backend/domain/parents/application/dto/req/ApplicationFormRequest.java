package com.yedu.backend.domain.parents.application.dto.req;

import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.parents.domain.entity.constant.Gender;
import com.yedu.backend.domain.parents.domain.entity.constant.Online;

import com.yedu.backend.domain.parents.domain.vo.DayTime;
import java.util.List;

public record ApplicationFormRequest(
        String phoneNumber,
        String age,
        ClassType wantedSubject,
        List<String> classGoals,
        Gender favoriteGender,
        String favoriteStyle,
        Online online,
        String classCount,
        String classTime,
        String district,
        String dong,
        String wantTime,
        String source,
        List<DayTime> dayTimes
) {
}
