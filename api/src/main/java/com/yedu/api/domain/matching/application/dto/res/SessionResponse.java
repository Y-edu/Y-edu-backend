package com.yedu.api.domain.matching.application.dto.res;

import com.yedu.api.domain.matching.domain.entity.ClassSession;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;

public record SessionResponse(Map<String, List<Schedule>> schedules) {

  @Builder(access = AccessLevel.PRIVATE)
  public record Schedule(
      Long classSessionId,
      @Schema(description = "휴강 여부") boolean cancel,
      @Schema(description = "휴강 사유") String cancelReason,
      @Schema(description = "완료 여부") boolean complete,
      @Schema(description = "이해도") String understanding,
      @Schema(description = "숙제 완료도") Integer homeworkPercentage,
      @Schema(description = "과외 일시") LocalDate classDate,
      @Schema(description = "과외 시간") LocalTime classStart,
      @Schema(description = "과외 소요시간") Integer classMinute) {}

  public static List<SessionResponse.Schedule> from(List<ClassSession> sessions) {
    return sessions.stream()
        .map(
            it ->
                Schedule.builder()
                    .classSessionId(it.getClassSessionId())
                    .cancel(it.isCancel())
                    .cancelReason(it.getCancelReason())
                    .complete(it.isCompleted())
                    .classDate(it.getSessionDate())
                    .classStart(it.getClassTime().getStart())
                    .understanding(it.getUnderstanding())
                    .homeworkPercentage(it.getHomeworkPercentage())
                    .classMinute(it.getClassTime().getClassMinute())
                    .build())
        .toList();
  }

  public static SessionResponse empty() {
    return new SessionResponse(Map.of());
  }
}
