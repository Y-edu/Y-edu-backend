package com.yedu.backend.domain.parents.application.mapper;

import com.yedu.backend.domain.parents.application.dto.res.ApplicationFormTimeTableResponse;
import com.yedu.backend.domain.parents.domain.entity.ApplicationFormAvailable;
import com.yedu.backend.domain.parents.domain.vo.DayTime;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

@UtilityClass
public class ApplicationFormAvailableMapper {

  public static ApplicationFormTimeTableResponse map(
      List<ApplicationFormAvailable> applicationFormAvailables) {
    if (CollectionUtils.isEmpty(applicationFormAvailables)) {
      return ApplicationFormTimeTableResponse.empty();
    }
    Map<Day, List<LocalTime>> groupedByDay =
        applicationFormAvailables.stream()
            .collect(
                Collectors.groupingBy(
                    ApplicationFormAvailable::getDay,
                    Collectors.mapping(
                        ApplicationFormAvailable::getAvailableTime, Collectors.toList())));

    List<DayTime> times =
        groupedByDay.entrySet().stream()
            .map(entry -> new DayTime(entry.getKey(), entry.getValue()))
            .toList();

    return ApplicationFormTimeTableResponse.builder()
        .applicationFormId(applicationFormAvailables.get(0).getApplicationFormId())
        .dayTimes(times)
        .build();
  }

  public static List<ApplicationFormAvailable> map(
      List<DayTime> dayTimes, String applicationFormId) {
    return dayTimes.stream()
        .flatMap(
            dayTime ->
                dayTime.getTimes().stream()
                    .map(
                        time ->
                            ApplicationFormAvailable.builder()
                                .applicationFormId(applicationFormId)
                                .day(dayTime.getDay())
                                .availableTime(time)
                                .build()))
        .toList();
  }
}
