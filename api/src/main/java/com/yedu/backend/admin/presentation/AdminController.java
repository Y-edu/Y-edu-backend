package com.yedu.backend.admin.presentation;

import com.yedu.backend.admin.application.dto.req.LoginRequest;
import com.yedu.backend.admin.application.dto.req.ParentsKakaoNameRequest;
import com.yedu.backend.admin.application.dto.req.ProposalTeacherRequest;
import com.yedu.backend.admin.application.dto.req.RecommendTeacherRequest;
import com.yedu.backend.admin.application.dto.req.TeacherIssueRequest;
import com.yedu.backend.admin.application.dto.req.TeacherSearchRequest;
import com.yedu.backend.admin.application.dto.req.TeacherVideoRequest;
import com.yedu.backend.admin.application.dto.res.AllAlarmTalkResponse;
import com.yedu.backend.admin.application.dto.res.AllApplicationResponse;
import com.yedu.backend.admin.application.dto.res.AllFilteringTeacher;
import com.yedu.backend.admin.application.dto.res.ClassDetailsResponse;
import com.yedu.backend.admin.application.dto.res.CommonParentsResponse;
import com.yedu.backend.admin.application.usecase.AdminAuthUseCase;
import com.yedu.backend.admin.application.usecase.AdminInfoUseCase;
import com.yedu.backend.admin.application.usecase.AdminManageUseCase;
import com.yedu.backend.admin.domain.entity.Admin;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;
import com.yedu.backend.global.config.security.jwt.dto.JwtResponse;
import com.yedu.common.type.ClassType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "ADMIN Controller", description = "로그인 제외 모든 API는 토큰이 필요합니다.")
public class AdminController {
    private final AdminInfoUseCase adminInfoUseCase;
    private final AdminManageUseCase adminManageUseCase;
    private final AdminAuthUseCase adminAuthUseCase;

    @GetMapping("/all/matching")
    @Operation(summary = "매칭 관리 - 들어온 모든 신청서 조회", description = "모든 들어온 신청서을 조회하는 API")
    public ResponseEntity<AllApplicationResponse> allApplication() {
        AllApplicationResponse allApplication = adminInfoUseCase.getAllApplication();
        return ResponseEntity.ok(allApplication);
    }

    @PutMapping("/matching/{applicationFormId}")
    @Operation(summary = "매칭 관리 - 처리상태 반대로 변경", description = "해당 신청서의 현상태 변경 API - 완료는 미완료로, 미완료는 완료로")
    public ResponseEntity updateProceedStatus(@PathVariable String applicationFormId) {
        adminManageUseCase.updateProceedStatus(applicationFormId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/details/matching/parents/{parentsId}")
    @Operation(summary = "신청서 상세조회 - 학부모 카카오 이름 변경", description = "특정 학부모 카카오 이름 변경 API")
    public ResponseEntity updateParentsName(@PathVariable long parentsId, @RequestBody ParentsKakaoNameRequest request) {
        adminManageUseCase.updateParentsKakaoName(parentsId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/teacher/issue/{teacherId}")
    @Operation(summary = "선생님 검색 - 선생님 간단 비고 수정", description = "선생님 간단 비고 수정 API")
    public ResponseEntity updateTeacherIssue(@PathVariable long teacherId, @RequestBody TeacherIssueRequest request) {
        adminManageUseCase.updateTeacherIssue(teacherId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/teacher/video/{teacherId}")
    @Operation(summary = "선생님 검색 - 선생님 유튜브 링크 수정", description = "선생님 유튜브 링크 수정 API")
    public ResponseEntity updateTeacherIssue(@PathVariable long teacherId, @RequestBody TeacherVideoRequest request) {
        adminManageUseCase.updateTeacherVideo(teacherId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/details/matching/parents/{applicationFormId}")
    @Operation(summary = "신청서 상세조회 - 매칭 학부모 정보", description = "학부모 정보 (신청건ID, 카카오 이름, 전화번호 등등) 조회 API")
    public ResponseEntity<CommonParentsResponse> commonParents(@PathVariable String applicationFormId) {
        CommonParentsResponse parentsInfo = adminInfoUseCase.getParentsInfo(applicationFormId);
        return ResponseEntity.ok(parentsInfo);
    }

    @GetMapping("/details/matching/alarm/{applicationFormId}")
    @Operation(summary = "신청서 상세조회 - 매칭건 알림톡 현황 조회", description = "신청서 알림톡 현황 조회 API")
    public ResponseEntity<AllAlarmTalkResponse> allAlarmTalk(@PathVariable String applicationFormId) {
        AllAlarmTalkResponse alarmTalkInfo = adminInfoUseCase.getAlarmTalkInfo(applicationFormId);
        return ResponseEntity.ok(alarmTalkInfo);
    }

    @GetMapping("/details/matching/class/{applicationFormId}")
    @Operation(summary = "신청서 상세조회 - 신청서 수업 조건 조회", description = "신청서 수업 상세 정보 조회 API")
    public ResponseEntity<ClassDetailsResponse> classDetails(@PathVariable String applicationFormId) {
        ClassDetailsResponse classDetails = adminInfoUseCase.getClassDetails(applicationFormId);
        return ResponseEntity.ok(classDetails);
    }

    @GetMapping("/details/matching/search/{applicationFormId}")
    @Operation(summary = "선생님 검색", description = "선생님 검색 API")
    public ResponseEntity<AllFilteringTeacher> searchTeachers(
            @RequestParam(required = false) List<String> districts,
            @RequestParam(required = false) List<ClassType> subjects,
            @RequestParam(required = false) List<String> universities,
            @RequestParam(required = false) List<TeacherGender> genders,
            @RequestParam(required = false) String search, @PathVariable String applicationFormId) {
        TeacherSearchRequest request = new TeacherSearchRequest(districts, subjects, universities, genders, search);
        AllFilteringTeacher allFilteringTeacher = adminInfoUseCase.searchAllTeacher(applicationFormId, request);
        return ResponseEntity.ok(allFilteringTeacher);
    }

    @PostMapping("/details/matching/recommend")
    @Operation(summary = "신청서 상세조회 - 학부모에게 선생님 제안 알림톡 전송", description = "학부모에게 선생님 제안 알림톡 전송 API")
    public ResponseEntity recommendTeacher(@RequestBody RecommendTeacherRequest request) {
        adminManageUseCase.recommendTeacher(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/details/matching/proposal/{applicationFormId}")
    @Operation(summary = "신청서 상세조회 - 선생님 검색시 선생님에게 과외 제안 알림톡 전송", description = "선생님에게 과외 제안 알림톡 전송 API")
    public ResponseEntity proposalTeacher(@PathVariable String applicationFormId, @RequestBody ProposalTeacherRequest request) {
        adminManageUseCase.proposalTeacher(applicationFormId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request, HttpServletResponse httpServletResponse) {
        JwtResponse jwtResponse = adminAuthUseCase.loginAdmin(request, httpServletResponse);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity logout(@AuthenticationPrincipal Admin admin) {
        adminAuthUseCase.logout(admin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/regenerate")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<JwtResponse> regenerate(@AuthenticationPrincipal Admin admin, HttpServletResponse response, HttpServletRequest request) {
        JwtResponse jwtResponse = adminAuthUseCase.regenerate(admin, request, response);
        return ResponseEntity.ok(jwtResponse);
    }
}
