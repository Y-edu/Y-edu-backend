package com.yedu.api.domain.matching.application.dto.req;

import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;
import java.util.List;

public record CreateScheduleRequest(
    String token,
    List<Schedule> schedules
) {

  public record Schedule(
      Day day,
      LocalTime start,
      Integer classMinute) {}

}
