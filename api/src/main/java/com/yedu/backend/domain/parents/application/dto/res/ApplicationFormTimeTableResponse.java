package com.yedu.backend.domain.parents.application.dto.res;

import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ApplicationFormTimeTableResponse(
    String applicationFormId,
    List<Time> times
) {
  public record Time(
      Day day,
      List<LocalTime> times
  ){

  }

  public static ApplicationFormTimeTableResponse empty() {
    return ApplicationFormTimeTableResponse.builder().build();
  }

}
