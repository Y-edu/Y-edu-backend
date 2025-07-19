package com.yedu.api.domain.matching.application.dto.res;

import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.domain.Page;

public record SessionResponse(Map<String, ScheduleInfo> schedules, Map<String, MatchingStatus> matchingStatuses) {

  public record ScheduleInfo(
      Page<Schedule> schedules,
      boolean send
  ){

  }

  @Builder(access = AccessLevel.PRIVATE)
  public record Schedule(
      Long classSessionId,
      @Schema(description = "휴강 여부") boolean cancel,
      @Schema(description = "당일 휴강 여부") boolean isTodayCancel,
      @Schema(description = "휴강 사유") String cancelReason,
      @Schema(description = "완료 여부") boolean complete,
      @Schema(description = "이해도") String understanding,
      @Schema(description = "숙제") String homework,
      @Schema(description = "과외 일시") LocalDate classDate,
      @Schema(description = "과외 시간") LocalTime classStart,
      @Schema(description = "현재 회차 정보") Integer currentRound,
      @Schema(description = "최대 회차 정보") Integer maxRound,
      @Schema(description = "과외 소요시간") Integer classMinute) {}

  public static Page<SessionResponse.Schedule> from(Page<ClassSession> sessions, Integer maxRound) {
    return sessions.map(
        it ->
            Schedule.builder()
                .classSessionId(it.getClassSessionId())
                .cancel(it.isCancel())
                .isTodayCancel(it.isTodayCancel())
                .cancelReason(it.getCancelReason())
                .complete(it.isCompleted())
                .classDate(it.getSessionDate())
                .classStart(it.getClassTime().getStart())
                .understanding(it.getUnderstanding())
                .homework(it.getHomework())
                .currentRound(it.getRound())
                .maxRound(maxRound)
                .classMinute(it.getClassTime().getClassMinute())
                .build());
  }

  public static SessionResponse empty() {
    return new SessionResponse(Map.of(), Map.of());
  }
}
