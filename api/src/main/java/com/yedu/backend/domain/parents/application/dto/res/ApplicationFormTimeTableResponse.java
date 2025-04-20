package com.yedu.backend.domain.parents.application.dto.res;

import com.yedu.backend.domain.parents.domain.vo.DayTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ApplicationFormTimeTableResponse(String applicationFormId, List<DayTime> dayTimes) {

  public static ApplicationFormTimeTableResponse empty() {
    return ApplicationFormTimeTableResponse.builder().build();
  }
}
