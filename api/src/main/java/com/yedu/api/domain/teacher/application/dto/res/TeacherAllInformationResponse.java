package com.yedu.api.domain.teacher.application.dto.res;

import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import com.yedu.common.type.ClassType;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record TeacherAllInformationResponse(
    Long matchingId,
    ClassType subject,
    String profile,
    String nickName,
    String classCount,
    String classTime,
    List<String> districts,
    Map<Day, List<LocalTime>> available,
    String comment,
    String introduce,
    int teachingHistory,
    List<String> teachingExperiences,
    List<String> foreignExperiences,
    String university,
    String major,
    String highSchool,
    String teachingStyle1,
    String teachingStyleInfo1,
    String teachingStyle2,
    String teachingStyleInfo2,
    String teachingStyle,
    String video) {}
