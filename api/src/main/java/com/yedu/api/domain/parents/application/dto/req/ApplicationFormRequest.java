package com.yedu.api.domain.parents.application.dto.req;

import com.yedu.api.domain.parents.domain.entity.constant.Gender;
import com.yedu.api.domain.parents.domain.entity.constant.Online;
import com.yedu.api.domain.parents.domain.vo.DayTime;
import com.yedu.common.type.ClassType;
import java.util.List;

public record ApplicationFormRequest(
    String phoneNumber,
    String age,
    ClassType wantedSubject,
    String wantedTime,
    List<String> classGoals,
    Gender favoriteGender,
    String favoriteStyle,
    Online online,
    String classCount,
    String classTime,
    String district,
    String dong,
    String source,
    List<DayTime> dayTimes) {}
