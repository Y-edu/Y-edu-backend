package com.yedu.api.domain.matching.presentation;

import com.yedu.api.domain.matching.application.dto.req.CancelSessionRequest;
import com.yedu.api.domain.matching.application.dto.req.ChangeSessionDateRequest;
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
import com.yedu.api.domain.matching.application.dto.res.RetrieveSessionDateResponse;
import com.yedu.api.domain.matching.application.dto.res.SessionResponse;
import com.yedu.api.domain.matching.application.usecase.ClassScheduleMatchingUseCase;
import com.yedu.api.domain.matching.domain.entity.constant.CancelReason;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
@Tag(name = "CLASS_MATCHING_SCHEDULE Controller", description = "과외 일정 관련 API")
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
  @Operation(
      summary = "과외 정규 일정 조회 API",
      description = "토큰으로 과외 일정을 조회합니다. 설정된 과외 시간/날짜가 없는 경우 빈 객체가 응답됩니다",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<List<RetrieveScheduleResponse>> retrieveSchedule(String token) {
    List<RetrieveScheduleResponse> response = scheduleMatchingUseCase.retrieveSchedules(token);

    return ResponseEntity.ok(response);
  }

  @PutMapping("/schedules")
  @Operation(
      summary = "과외 정규 일정 설정 API",
      description = "토큰으로 과외 일정을 덮어씁니다",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<SessionResponse> createSchedule(
      @RequestBody CreateScheduleRequest request) {
    SessionResponse response = scheduleMatchingUseCase.create(request);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/sessions")
  @Operation(
      summary = "과외 실제 일정 조회 API",
      description = "설정된 과외 일정이 없다면 생성 후 반환합니다",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<SessionResponse> retrieveSession(
      String token,
      Boolean isComplete,
      @PageableDefault(sort = "sessionDate", direction = Direction.DESC) Pageable pageable) {
    if (isComplete != null && !isComplete) {
      pageable =
          PageRequest.of(
              pageable.getPageNumber(),
              pageable.getPageSize(),
              Sort.by(Sort.Direction.ASC, "sessionDate"));
    }

    SessionResponse response = scheduleMatchingUseCase.retrieveSession(token, pageable, isComplete);

    return ResponseEntity.ok(response);
  }

  @PatchMapping("/sessions/{sessionId}/cancel")
  @Operation(
      summary = "수업 휴강 처리 API",
      description = "sessionId로 과외를 휴강 처리합니다",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<Void> cancelSession(
      @PathVariable Long sessionId,
      @RequestBody CancelSessionRequest cancelSessionRequest) {

    scheduleMatchingUseCase.cancelSession(sessionId, cancelSessionRequest);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/sessions/{sessionId}/revert-cancel")
  @Operation(
      summary = "수업 휴강 취소 API",
      description = "sessionId로 과외를 휴강 취소합니다",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<Void> revertCancelSession(@PathVariable Long sessionId) {

    scheduleMatchingUseCase.revertCancelSession(sessionId);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/sessions/{sessionId}/complete")
  @Operation(
      summary = "수업 완료 API",
      description = "sessionId로 과외를 완료 처리합니다",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<Void> completeSession(
      @PathVariable Long sessionId, @RequestBody CompleteSessionRequest completeSessionRequest) {
    scheduleMatchingUseCase.completeSession(sessionId, completeSessionRequest);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/token/sessions/complete")
  @Operation(
      summary = "토큰 기반 수업 완료 API",
      description = "토큰으로 과외를 완료 처리합니다",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<Void> completeSessionByToken(
      @RequestBody CompleteSessionTokenRequest completeSessionTokenRequest) {
    scheduleMatchingUseCase.completeSessionByToken(completeSessionTokenRequest);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/token/sessions")
  @Operation(
      summary = "토큰 기반 수업 일시, 과외 완료 여부 조회 API",
      description = "토큰으로 수업 일시와 과외 완료 여부를 조회합니다",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<RetrieveSessionDateResponse> retrieveSessionDateByToken(String token) {
    RetrieveSessionDateResponse response =
        scheduleMatchingUseCase.retrieveSessionDateByToken(token);

    return ResponseEntity.ok(response);
  }

  @PatchMapping("/sessions/{sessionId}/change")
  @Operation(
      summary = "수업 일자 변경 API",
      description = "sessionId로 과외 수업 일자를 변경합니다",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<Void> changeSessionDate(
      @PathVariable Long sessionId,
      @RequestBody ChangeSessionDateRequest changeSessionDateRequest) {
    scheduleMatchingUseCase.changeSessionDate(sessionId, changeSessionDateRequest);

    return ResponseEntity.noContent().build();
  }
}
