package com.yedu.backend.admin.presentation;

import com.yedu.backend.admin.application.dto.req.*;
import com.yedu.backend.admin.application.dto.res.*;
import com.yedu.backend.admin.application.usecase.AdminAuthUseCase;
import com.yedu.backend.admin.application.usecase.AdminInfoUseCase;
import com.yedu.backend.admin.application.usecase.AdminManageUseCase;
import com.yedu.backend.admin.domain.entity.Admin;
import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;
import com.yedu.backend.global.config.security.jwt.dto.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final AdminInfoUseCase adminInfoUseCase;
    private final AdminManageUseCase adminManageUseCase;
    private final AdminAuthUseCase adminAuthUseCase;

    @GetMapping("/all/matching")
    public ResponseEntity<AllApplicationResponse> allApplication() {
        AllApplicationResponse allApplication = adminInfoUseCase.getAllApplication();
        return ResponseEntity.ok(allApplication);
    }

    @PutMapping("/matching/{applicationFormId}")
    public ResponseEntity updateProceedStatus(@PathVariable String applicationFormId) {
        adminManageUseCase.updateProceedStatus(applicationFormId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/details/matching/parents/{parentsId}")
    public ResponseEntity updateProceed(@PathVariable long parentsId, @RequestBody ParentsKakaoNameRequest request) {
        adminManageUseCase.updateParentsKakaoName(parentsId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/teacher/issue/{teacherId}")
    public ResponseEntity updateTeacherIssue(@PathVariable long teacherId, @RequestBody TeacherIssueRequest request) {
        adminManageUseCase.updateTeacherIssue(teacherId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/details/matching/parents/{applicationFormId}")
    public ResponseEntity<CommonParentsResponse> commonParents(@PathVariable String applicationFormId) {
        CommonParentsResponse parentsInfo = adminInfoUseCase.getParentsInfo(applicationFormId);
        return ResponseEntity.ok(parentsInfo);
    }

    @GetMapping("/details/matching/alarm/{applicationFormId}")
    public ResponseEntity<AllAlarmTalkResponse> allAlarmTalk(@PathVariable String applicationFormId) {
        AllAlarmTalkResponse alarmTalkInfo = adminInfoUseCase.getAlarmTalkInfo(applicationFormId);
        return ResponseEntity.ok(alarmTalkInfo);
    }

    @GetMapping("/details/matching/class/{applicationFormId}")
    public ResponseEntity<ClassDetailsResponse> classDetails(@PathVariable String applicationFormId) {
        ClassDetailsResponse classDetails = adminInfoUseCase.getClassDetails(applicationFormId);
        return ResponseEntity.ok(classDetails);
    }

    @GetMapping("/details/matching/search")
    public ResponseEntity<AllFilteringTeacher> searchTeachers(
            @RequestParam(required = false) List<String> districts,
            @RequestParam(required = false) List<ClassType> subjects,
            @RequestParam(required = false) List<String> universities,
            @RequestParam(required = false) List<TeacherGender> genders,
            @RequestParam(required = false) String search) {
        TeacherSearchRequest request = new TeacherSearchRequest(districts, subjects, universities, genders, search);
        AllFilteringTeacher allFilteringTeacher = adminInfoUseCase.searchAllTeacher(request);
        return ResponseEntity.ok(allFilteringTeacher);
    }

    @PostMapping("/details/matching/recommend/{applicationFormId}")
    public ResponseEntity recommendTeacher(@PathVariable String applicationFormId, @RequestBody RecommendTeacherRequest request) {
        adminManageUseCase.recommendTeacher(applicationFormId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request, HttpServletResponse httpServletResponse) {
        JwtResponse jwtResponse = adminAuthUseCase.loginAdmin(request, httpServletResponse);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@AuthenticationPrincipal Admin admin) {
        adminAuthUseCase.logout(admin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/regenerate")
    public ResponseEntity<JwtResponse> regenerate(@AuthenticationPrincipal Admin admin, HttpServletResponse response, HttpServletRequest request) {
        JwtResponse jwtResponse = adminAuthUseCase.regenerate(admin, request, response);
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/test")
    public ResponseEntity test(@AuthenticationPrincipal Admin admin) {
        if (admin == null)
            throw new IllegalArgumentException();
        return ResponseEntity.ok("인증에 성공하였습니다.");
    }
}
