package com.yedu.api.admin.application.dto.res;

import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;

public record ScheduledClass(Day day, LocalTime startTime, Integer classTime) {}
