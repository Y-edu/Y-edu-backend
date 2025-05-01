package com.yedu.backend.domain.teacher.presentation;

import com.yedu.backend.domain.teacher.application.dto.req.AlarmTalkChangeRequest;
import com.yedu.backend.domain.teacher.application.dto.req.AvailableChangeRequest;
import com.yedu.backend.domain.teacher.application.dto.req.AvailableChangeTokenRequest;
import com.yedu.backend.domain.teacher.application.dto.req.DistrictChangeRequest;
import com.yedu.backend.domain.teacher.application.dto.req.TeacherContractRequest;
import com.yedu.backend.domain.teacher.application.dto.req.TeacherInfoFormRequest;
import com.yedu.backend.domain.teacher.application.dto.req.TeacherProfileFormRequest;
import com.yedu.backend.domain.teacher.application.dto.res.DistrictAndTimeResponse;
import com.yedu.backend.domain.teacher.application.dto.res.EnglishCurriculumResponse;
import com.yedu.backend.domain.teacher.application.dto.res.MathCurriculumResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherCommonsInfoResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherEnglishResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherInfoResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherMathResponse;
import com.yedu.backend.domain.teacher.application.usecase.TeacherInfoUseCase;
import com.yedu.backend.domain.teacher.application.usecase.TeacherManageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
@Tag(name = "TEACHER Controller")
public class TeacherController {
  private final TeacherManageUseCase manageUseCase;
  private final TeacherInfoUseCase infoUseCase;

  @PostMapping("/form")
  @Operation(summary = "구글폼 - 선생님 기본정보 신청건 API")
  public ResponseEntity saveByForm(@RequestBody TeacherInfoFormRequest request) {
    manageUseCase.saveTeacher(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/form/profile")
  @Operation(summary = "구글폼 - 선생님 프로필 사진 및 영상 API")
  public ResponseEntity saveProfileByForm(
      @RequestPart TeacherProfileFormRequest request, @RequestPart MultipartFile profile) {
    manageUseCase.saveTeacherProfile(profile, request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/form/contract")
  @Operation(summary = "구글폼 - 선생님 계약서 API")
  public ResponseEntity submitContract(@RequestBody TeacherContractRequest request) {
    manageUseCase.submitContract(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/details/info/{teacherId}")
  @Operation(summary = "선생님 프로필 정보 - 선생님 프로필 사진 및 닉네임 조회 API")
  public ResponseEntity<TeacherCommonsInfoResponse> commonsInfo(@PathVariable long teacherId) {
    TeacherCommonsInfoResponse teacherCommonsInfoResponse =
        infoUseCase.teacherCommonsInfo(teacherId);
    return ResponseEntity.ok(teacherCommonsInfoResponse);
  }

  @GetMapping("/details/math/teacher/{teacherId}")
  @Operation(summary = "선생님 프로필 정보 - 수학 선생님 정보 조회 API")
  public ResponseEntity<TeacherMathResponse> mathTeacherInfo(@PathVariable long teacherId) {
    TeacherMathResponse teacherMathResponse = infoUseCase.teacherMathDetails(teacherId);
    return ResponseEntity.ok(teacherMathResponse);
  }

  @GetMapping("/details/english/teacher/{teacherId}")
  @Operation(summary = "선생님 프로필 정보 - 영어 선생님 정보 조회 API")
  public ResponseEntity<TeacherEnglishResponse> englishTeacherInfo(@PathVariable long teacherId) {
    TeacherEnglishResponse teacherEnglishResponse = infoUseCase.teacherEnglishDetails(teacherId);
    return ResponseEntity.ok(teacherEnglishResponse);
  }

  @GetMapping("/details/math/class/{teacherId}")
  @Operation(summary = "선생님 프로필 정보 - 수학 수업 정보 조회 API")
  public ResponseEntity<MathCurriculumResponse> mathCurriculumInfo(@PathVariable long teacherId) {
    MathCurriculumResponse mathCurriculumResponse = infoUseCase.curriculumMath(teacherId);
    return ResponseEntity.ok(mathCurriculumResponse);
  }

  @GetMapping("/details/english/class/{teacherId}")
  @Operation(summary = "선생님 프로필 정보 - 영어 수업 정보 조회 API")
  public ResponseEntity<EnglishCurriculumResponse> englishCurriculumInfo(
      @PathVariable long teacherId) {
    EnglishCurriculumResponse englishCurriculumResponse = infoUseCase.curriculumEnglish(teacherId);
    return ResponseEntity.ok(englishCurriculumResponse);
  }

  @GetMapping("/details/available/{teacherId}")
  @Operation(summary = "선생님 프로필 정보 - 선생님 가능 지역 및 시간 조회 API")
  public ResponseEntity<DistrictAndTimeResponse> availableInfo(@PathVariable long teacherId) {
    DistrictAndTimeResponse districtAndTimeResponse = infoUseCase.districtAndTime(teacherId);
    return ResponseEntity.ok(districtAndTimeResponse);
  }

  @GetMapping("/info/{name}/{phoneNumber}")
  @Operation(summary = "선생님 수업 정보 수정 - 선생님 이름, 전화번호 기반 선생님 정보 조회")
  public ResponseEntity<TeacherInfoResponse> teacherMyPage(
      @PathVariable String name, @PathVariable String phoneNumber) {
    TeacherInfoResponse teacherInfoResponse = infoUseCase.teacherMyPage(name, phoneNumber);
    return ResponseEntity.ok(teacherInfoResponse);
  }

  @PutMapping("/info/active/talk")
  @Operation(summary = "선생님 수업 정보 수정 - 선생님 알림톡 수신 여부 수정 true/false")
  public ResponseEntity<Void> changeAlarmTalk(@RequestBody AlarmTalkChangeRequest request) {
    manageUseCase.changeAlarmTalkStatus(request);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/info/district")
  @Operation(summary = "선생님 수업 정보 수정 - 선생님 가능 지역 변경")
  public ResponseEntity<Void> changeDistrict(@RequestBody DistrictChangeRequest request) {
    manageUseCase.changeDistrict(request);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/info/available")
  @Operation(summary = "선생님 수업 정보 수정 - 선생님 가능 시간 변경")
  public ResponseEntity<Void> changeAvailable(@RequestBody AvailableChangeRequest request) {
    manageUseCase.changeAvailable(request);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/token/info/available")
  @Operation(summary = "선생님 수업 정보 수정 - 선생님 가능 지역 변경 (알림톡 토큰 사용하여 변경)")
  public ResponseEntity<Void> changeAvailable(@RequestBody AvailableChangeTokenRequest request) {
    manageUseCase.changeAvailableByToken(request);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/token/info/available")
  @Operation(summary = "선생님 수업 정보 조회 (알림톡 토큰 조회)")
  public ResponseEntity<DistrictAndTimeResponse> retrieveAvailable(String token) {
    DistrictAndTimeResponse districtAndTimeResponse = infoUseCase.retrieveAvailableByToken(token);

    return ResponseEntity.ok(districtAndTimeResponse);
  }
}
