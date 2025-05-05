package com.yedu.api.domain.parents.application.dto.res;

import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record ApplicationFormTimeTableResponse(
    String applicationFormId, Map<Day, List<LocalTime>> availables) {

  public static ApplicationFormTimeTableResponse empty() {
    return ApplicationFormTimeTableResponse.builder().build();
  }
}
