package com.yedu.api.domain.matching.application.dto.res;

import com.yedu.api.domain.matching.domain.vo.ClassTime;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import java.util.List;
import java.util.Map;

public record RetrieveScheduleResponse(
    String applicationFormId,
    Long classMatchingId,
    boolean send,
    Map<Day, List<ClassTime>> schedules) {

  public static RetrieveScheduleResponse empty(
      String applicationFormId, Long classMatchingId, boolean send) {
    return new RetrieveScheduleResponse(applicationFormId, classMatchingId, send, Map.of());
  }
}
