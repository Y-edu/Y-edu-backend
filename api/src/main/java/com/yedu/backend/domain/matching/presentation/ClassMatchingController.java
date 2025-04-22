package com.yedu.backend.domain.matching.presentation;

import com.yedu.backend.domain.matching.application.dto.req.ClassMatchingRefuseRequest;
import com.yedu.backend.domain.matching.application.dto.res.ClassMatchingForTeacherResponse;
import com.yedu.backend.domain.matching.application.usecase.ClassMatchingInfoUseCase;
import com.yedu.backend.domain.matching.application.usecase.ClassMatchingManageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
@Tag(name = "CLASS_MATCHING Controller")
public class ClassMatchingController {
  private final ClassMatchingInfoUseCase matchingInfoUseCase;
  private final ClassMatchingManageUseCase matchingManageUseCase;

  @GetMapping("/application")
  @Operation(
      summary = "과외 공지 선생님 알림톡 - 과외 신청 및 보류 페이지 조회 API",
      description = "선생님에게 보여지는 과외 신청 및 보유 페이지 조회 API")
  public ResponseEntity<ClassMatchingForTeacherResponse> applicationToTeacher(String token) {
    ClassMatchingForTeacherResponse classMatchingForTeacherResponse =
        matchingInfoUseCase.applicationFormToTeacher(token);
    return ResponseEntity.ok(classMatchingForTeacherResponse);
  }

  @PutMapping("/application/refuse")
  @Operation(summary = "과외 공지 선생님 알림톡 - 과외 거절 API")
  public ResponseEntity<Void> refuseApplication(
      String token, @RequestBody ClassMatchingRefuseRequest request) {
    matchingManageUseCase.refuseClassMatching(token, request);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/application/accept")
  @Operation(summary = "과외 공지 선생님 알림톡 - 과외 수락 API")
  public ResponseEntity<Void> acceptApplication(String token) {
    matchingManageUseCase.acceptClassMatching(token);

    return ResponseEntity.noContent().build();
  }

  @PostMapping("/remind")
  public ResponseEntity<Void> matchingRemind() {
    matchingManageUseCase.remindClassMatching();

    return ResponseEntity.noContent().build();
  }
}
