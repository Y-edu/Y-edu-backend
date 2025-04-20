package com.yedu.backend.admin.application.dto.res;

import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;

public record ScheduledClass(Day day, LocalTime startTime, Integer classTime) {}
