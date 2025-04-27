package com.yedu.backend.domain.teacher.presentation;

import com.yedu.backend.domain.teacher.application.usecase.TeacherBatchUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

//  @PostMapping("/sync/request/availability-time")
  @Operation(summary = "선생님 가능 시간 갱신 요청 알림톡")
  public ResponseEntity<Void> remindAvailableTime() {
    teacherBatchUseCase.remindAvailableTime();

    return ResponseEntity.noContent().build();
  }
}
