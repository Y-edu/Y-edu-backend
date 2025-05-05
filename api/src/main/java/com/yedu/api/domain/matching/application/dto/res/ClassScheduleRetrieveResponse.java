package com.yedu.api.domain.matching.application.dto.res;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.vo.ClassTime;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ClassScheduleRetrieveResponse(
    Boolean exist,
    Long classMatchingId,
    Long classScheduleManagementId,
    String textBook,
    List<Schedule> schedules,
    FirstDay firstDay) {

  public record Schedule(
      Day day, @Schema(example = "13:00") LocalTime start, Integer classMinute) {}

  public record FirstDay(LocalDate date, @Schema(example = "12:00") LocalTime start) {}

  public static ClassScheduleRetrieveResponse empty() {
    return ClassScheduleRetrieveResponse.builder().exist(false).build();
  }

  public static ClassScheduleRetrieveResponse of(ClassManagement classManagement) {
    ClassTime firstDayClassTime = classManagement.getClassTime();

    return ClassScheduleRetrieveResponse.builder()
        .exist(true)
        .classMatchingId(classManagement.getClassMatching().getClassMatchingId())
        .classScheduleManagementId(classManagement.getClassManagementId())
        .textBook(classManagement.getTextbook())
        .schedules(
            classManagement.getSchedules().stream()
                .map(
                    schedule -> {
                      ClassTime classTime = schedule.getClassTime();
                      return new Schedule(
                          schedule.getDay(), classTime.getStart(), classTime.getClassMinute());
                    })
                .toList())
        .firstDay(
            firstDayClassTime != null
                ? new FirstDay(classManagement.getFirstDay(), firstDayClassTime.getStart())
                : null)
        .build();
  }
}
