package com.yedu.api.domain.matching.presentation;

import com.yedu.api.domain.matching.application.dto.req.ClassMatchingRefuseRequest;
import com.yedu.api.domain.matching.application.dto.res.ApplicationFormResponse;
import com.yedu.api.domain.matching.application.dto.res.ClassMatchingForTeacherResponse;
import com.yedu.api.domain.matching.application.usecase.ClassMatchingInfoUseCase;
import com.yedu.api.domain.matching.application.usecase.ClassMatchingManageUseCase;
import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
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

  @QueryMapping
  public List<ApplicationFormResponse> applicationFormByMatchingId(
      @Argument List<Long> matchingIds, @Argument List<MatchingStatus> matchingStatus) {
    return matchingInfoUseCase.applicationFormByMatchingId(matchingIds, matchingStatus);
  }

  @MutationMapping
  public Boolean updateMatching(@Argument List<Long> matchingIds, @Argument MatchingStatus matchingStatus) {
    matchingManageUseCase.updateMatching(matchingIds, matchingStatus);

    return Boolean.TRUE;
  }

  @MutationMapping
  public Boolean changeTeacher(@Argument Long matchingId, @Argument Long newTeacherId) {
    matchingManageUseCase.changeTeacher(matchingId, newTeacherId);

    return Boolean.TRUE;
  }

  @SchemaMapping(typeName = "ApplicationForm", field = "parent")
  public ApplicationFormResponse.Parents parent(final ApplicationFormResponse applicationForm) {
    return matchingInfoUseCase.parents(applicationForm);
  }

  @SchemaMapping(typeName = "ApplicationForm", field = "availableTimes")
  public List<ApplicationFormResponse.AvailableTime> availableTimes(
      final ApplicationFormResponse applicationForm) {
    return matchingInfoUseCase.availableTimes(applicationForm);
  }

  @SchemaMapping(typeName = "ApplicationForm", field = "classManagement")
  public ApplicationFormResponse.ClassManagement classManagement(
      final ApplicationFormResponse applicationForm) {
    return matchingInfoUseCase.classManagement(applicationForm);
  }

  @SchemaMapping(typeName = "Teacher", field = "english")
  public ApplicationFormResponse.TeacherEnglish english(
      final ApplicationFormResponse.Teacher teacher) {
    return matchingInfoUseCase.english(teacher);
  }

  @SchemaMapping(typeName = "Teacher", field = "math")
  public ApplicationFormResponse.TeacherMath math(final ApplicationFormResponse.Teacher teacher) {
    return matchingInfoUseCase.math(teacher);
  }
}
