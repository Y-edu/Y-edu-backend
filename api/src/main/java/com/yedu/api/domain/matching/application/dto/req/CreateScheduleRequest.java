package com.yedu.api.domain.matching.application.dto.req;

import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.List;

public record CreateScheduleRequest(
    String token,
    Long classMatchingId,
    List<Schedule> schedules
) {

  public record Schedule(
      Day day,
      @Schema(example = "13:00") LocalTime start,
      @Schema(example = "50", description = "수업시간") Integer classMinute) {}

}
