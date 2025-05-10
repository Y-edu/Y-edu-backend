package com.yedu.api.domain.matching.presentation;

import com.yedu.api.domain.matching.application.dto.req.ClassScheduleConfirmRequest;
import com.yedu.api.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.api.domain.matching.application.dto.req.ClassScheduleRefuseRequest;
import com.yedu.api.domain.matching.application.dto.req.ClassScheduleRetrieveRequest;
import com.yedu.api.domain.matching.application.dto.req.CompleteSessionRequest;
import com.yedu.api.domain.matching.application.dto.req.CompleteSessionTokenRequest;
import com.yedu.api.domain.matching.application.dto.req.CreateScheduleRequest;
import com.yedu.api.domain.matching.application.dto.res.ClassScheduleMatchingResponse;
import com.yedu.api.domain.matching.application.dto.res.ClassScheduleRetrieveResponse;
import com.yedu.api.domain.matching.application.dto.res.RetrieveScheduleResponse;
import com.yedu.api.domain.matching.application.dto.res.SessionResponse;
import com.yedu.api.domain.matching.application.usecase.ClassScheduleMatchingUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "CLASS_MATCHING_SCHEDULE Controller", description = "매칭 후반부 자동화 스프린트에 사용될 API")
public class ClassMatchingScheduleController {

  private final ClassScheduleMatchingUseCase scheduleMatchingUseCase;

  @GetMapping("/matching/schedule")
  @Operation(
      summary = "상담 결과 조회 API",
      description =
          "classScheduleManagementId 또는 classMatchingId로 상담결과를 조회합니다. (둘 다 넘겨줄 경우 classScheduleManagementId로 조회합니다) ")
  public ResponseEntity<ClassScheduleRetrieveResponse> retrieveSchedule(
      @ParameterObject ClassScheduleRetrieveRequest request) {
    ClassScheduleRetrieveResponse response = scheduleMatchingUseCase.retrieve(request);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/matching/schedule")
  @Operation(summary = "상담 신청 API")
  public ResponseEntity<ClassScheduleMatchingResponse> requestScheduleMatch(
      @RequestBody ClassScheduleMatchingRequest request) {
    String key = scheduleMatchingUseCase.schedule(request);

    return ResponseEntity.ok(new ClassScheduleMatchingResponse(key));
  }

  @DeleteMapping("/matching/schedule")
  @Operation(summary = "상담 후 미진행 API")
  public ResponseEntity<Void> refuseScheduleMatch(@RequestBody ClassScheduleRefuseRequest request) {
    scheduleMatchingUseCase.refuse(request);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/matching/schedule")
  @Operation(summary = "상담 후 진행 API")
  public ResponseEntity<Void> confirmScheduleMatch(
      @RequestBody ClassScheduleConfirmRequest request) {
    scheduleMatchingUseCase.confirm(request);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/schedules")
  @Operation(summary = "과외 정규 일정 조회 API", description = "토큰으로 과외 일정을 조회합니다")
  public ResponseEntity<RetrieveScheduleResponse> retrieveSchedule(String token) {
    RetrieveScheduleResponse response = scheduleMatchingUseCase.retrieveSchedule(token);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/schedules")
  @Operation(summary = "과외 정규 일정 설정 API")
  public ResponseEntity<SessionResponse> createSchedule(
      @RequestBody CreateScheduleRequest request) {
    SessionResponse response = scheduleMatchingUseCase.create(request);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/sessions")
  @Operation(summary = "과외 실제 일정 조회 API")
  public ResponseEntity<SessionResponse> retrieveSession(String token) {
    SessionResponse response = scheduleMatchingUseCase.retrieveSession(token);

    return ResponseEntity.ok(response);
  }

  @PatchMapping("/sessions/{sessionId}/cancel")
  @Operation(summary = "수업 휴강 처리 API")
  public ResponseEntity<Void> cancelSession(
      @PathVariable Long sessionId, @RequestParam String cancelReason) {

    scheduleMatchingUseCase.cancelSession(sessionId, cancelReason);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/sessions/{sessionId}/revert-cancel")
  @Operation(summary = "수업 휴강 취소 API")
  public ResponseEntity<Void> revertCancelSession(@PathVariable Long sessionId) {

    scheduleMatchingUseCase.revertCancelSession(sessionId);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/sessions/{sessionId}/complete")
  @Operation(summary = "수업 완료 API")
  public ResponseEntity<Void> completeSession(
      @PathVariable Long sessionId, @RequestBody CompleteSessionRequest completeSessionRequest) {
    scheduleMatchingUseCase.completeSession(sessionId, completeSessionRequest);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/token/sessions/complete")
  @Operation(summary = "수업 완료 API")
  public ResponseEntity<Void> completeSessionByToken(
      @RequestBody CompleteSessionTokenRequest completeSessionTokenRequest) {
    scheduleMatchingUseCase.completeSessionByToken(completeSessionTokenRequest);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/sessions/{sessionId}/change")
  @Operation(summary = "수업 일자 변경 API")
  public ResponseEntity<Void> changeSessionDate(
      @PathVariable Long sessionId, LocalDate sessionDate, LocalTime start) {
    scheduleMatchingUseCase.changeSessionDate(sessionId, sessionDate, start);

    return ResponseEntity.noContent().build();
  }
}
