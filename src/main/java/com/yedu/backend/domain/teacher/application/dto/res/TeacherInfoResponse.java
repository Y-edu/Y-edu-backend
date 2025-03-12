package com.yedu.backend.domain.teacher.application.dto.res;

import com.yedu.backend.domain.teacher.domain.entity.constant.Day;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public record TeacherInfoResponse(
        String name,
        boolean alarmTalk,
        List<String> districts,
        Map<Day, List<LocalTime>> available
) {
}
