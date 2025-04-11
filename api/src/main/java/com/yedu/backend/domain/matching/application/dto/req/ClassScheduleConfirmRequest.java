package com.yedu.backend.domain.matching.application.dto.req;

import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ClassScheduleConfirmRequest(
    String classScheduleManagementId,
    @Schema(example = "수학의 정석", description = "교재")
    String textBook,
    List<Schedule> schedules,
    FirstDay firstDay
) {

  public record Schedule(
      Day day,
      @Schema(example = "13:00")
      LocalTime start,
      @Schema(example = "50", description = "수업시간")
      Integer classMinute
  ){

  }

  public record FirstDay(
      LocalDate date,
      @Schema(example = "12:00")
      LocalTime start
  ){

  }


}
