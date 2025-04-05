package com.yedu.backend.domain.matching.application.dto.req;

import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ClassScheduleConfirmRequest(
    String classScheduleManagementId,
    String textBook,
    List<Schedule> schedules,
    FirstDay firstDay
) {

  public record Schedule(
      Day day,
      LocalTime start,
      Integer classMinute
  ){

  }

  public record FirstDay(
      LocalDate date,
      LocalTime start,
      Integer classMinute
  ){

  }


}
