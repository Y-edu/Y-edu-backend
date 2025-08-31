package com.yedu.api.domain.parents.presentation;

import com.yedu.api.domain.parents.application.dto.req.AcceptChangeSessionRequest;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormChangeRequest;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormRequest;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormTimeTableRequest;
import com.yedu.api.domain.parents.application.dto.res.ApplicationFormTimeTableResponse;
import com.yedu.api.domain.parents.application.dto.res.ParentSessionResponse;
import com.yedu.api.domain.parents.application.usecase.ParentsManageUseCase;
import com.yedu.api.domain.teacher.application.usecase.TeacherChangeUsecase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/parents")
@RestController
@RequiredArgsConstructor
@Tag(name = "PARENTS Controller")
public class ParentsController {
  private final ParentsManageUseCase parentsManageUseCase;
  private final TeacherChangeUsecase teacherChangeUsecase;

  @PostMapping("/save/application")
  @Operation(summary = "Tally 제출 - 학부모 신청건 API")
  public ResponseEntity saveApplication(@RequestBody ApplicationFormRequest request) {
    parentsManageUseCase.saveParentsAndApplication(request, false, null);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/change/application")
  @Operation(summary = "Tally 제출 - 학부모 선생님 교체 신청건 API")
  public ResponseEntity<?> changeApplication(@RequestBody ApplicationFormChangeRequest request) {
    teacherChangeUsecase.change(request);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/resend/application")
  @Operation(summary = "관리자 직접 제출 - 학부모 신청건 재발송 API")
  public ResponseEntity resendApplication(@RequestBody ApplicationFormRequest request) {
    parentsManageUseCase.resendParentsAndApplication(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/timetable")
  @Operation(summary = "과외의 타임테이블 조회")
  public ResponseEntity<ApplicationFormTimeTableResponse> retrieveTimeTable(
      @Valid @ParameterObject ApplicationFormTimeTableRequest request) {
    ApplicationFormTimeTableResponse response = parentsManageUseCase.retrieveTimeTable(request);

    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/{phoneNumber}/sessions")
  @Operation(summary = "학부모의 과외 일정 조회")
  public ResponseEntity<List<ParentSessionResponse>> retrieveSessions(
      @PathVariable String phoneNumber) {
    List<ParentSessionResponse> response = parentsManageUseCase.retrieveSessions(phoneNumber);

    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/{phoneNumber}/sessions/change-form")
  @Operation(summary = "학부모의 과외 일시정지/교체 신청 접수")
  public ResponseEntity<Void> acceptChangeSessionForm(
      @PathVariable String phoneNumber,
      @RequestBody AcceptChangeSessionRequest acceptChangeSessionRequest) {
    parentsManageUseCase.acceptChangeSessionForm(phoneNumber, acceptChangeSessionRequest);

    return ResponseEntity.noContent().build();
  }
}
