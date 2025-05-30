package com.yedu.api.domain.matching.application.dto.req;

import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateScheduleRequest(
    String token, Long classMatchingId, List<Schedule> schedules, LocalDate changeStartDate) {

  public record Schedule(Day day, LocalTime start, Integer classMinute) {}
}
