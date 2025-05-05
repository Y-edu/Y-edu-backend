package com.yedu.api.domain.teacher.application.dto.res;

import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public record TeacherInfoResponse(
    String name, boolean alarmTalk, List<String> districts, Map<Day, List<LocalTime>> available) {}
