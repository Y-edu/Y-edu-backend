package com.yedu.api.domain.matching.application.dto.res;

import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.api.domain.parents.domain.entity.constant.Online;
import com.yedu.api.domain.parents.domain.vo.DayTime;
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
    //    List<DayTime> parentDayTimes,
    String wantedTime,
    List<DayTime> teacherDayTimes,
    MatchingStatus matchStatus) {}
