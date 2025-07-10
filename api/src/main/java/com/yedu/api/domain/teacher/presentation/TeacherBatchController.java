package com.yedu.api.domain.teacher.presentation;

import com.yedu.api.domain.teacher.application.usecase.TeacherBatchUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
@Tag(name = "TEACHER Controller")
public class TeacherBatchController {

  private final TeacherBatchUseCase teacherBatchUseCase;

  @PostMapping("/sync/request/availability-time")
  @Operation(summary = "선생님 가능 시간 갱신 요청 알림톡")
  public ResponseEntity<Void> remindAvailableTime() {
    teacherBatchUseCase.remindAvailableTime();

    return ResponseEntity.noContent().build();
  }

  @PostMapping("/notice/complete-talk-change")
  @Operation(
      summary = "완료톡 변경 안내 알림톡 발송",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<Void> completeTalkNotice(Set<Long> teacherIds) {
    teacherBatchUseCase.completeTalkNotice(teacherIds);

    return ResponseEntity.noContent().build();
  }

  @PostMapping("/request/complete-talk/with-schedule")
  @Operation(
      summary = "시간/날짜 등록된 경우 완료톡 알림톡 발송",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<Void> completeTalkToTeacherWithSchedule() {
    teacherBatchUseCase.completeTalkToTeacherWithSchedule();

    return ResponseEntity.noContent().build();
  }

  @PostMapping("/request/complete-talk/remind")
  @Operation(
      summary = "완료톡 알림톡 리마인드 발송",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<Void> remind() {
    teacherBatchUseCase.remind();

    return ResponseEntity.noContent().build();
  }

  @PostMapping("/request/complete-talk/without-schedule")
  @Operation(
      summary = "시간/날짜 미등록된 경우 완료톡 알림톡 발송",
      tags = {"완료톡 관련 API"})
  public ResponseEntity<Void> completeTalkToTeacherWithNoSchedule() {
    teacherBatchUseCase.completeTalkToTeacherWithNoSchedule();

    return ResponseEntity.noContent().build();
  }
}
