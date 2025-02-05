package com.yedu.backend.admin.presentation;

import com.yedu.backend.admin.application.dto.req.ParentsKakaoNameRequest;
import com.yedu.backend.admin.application.dto.req.TeacherIssueRequest;
import com.yedu.backend.admin.application.dto.req.TeacherSearchRequest;
import com.yedu.backend.admin.application.dto.res.*;
import com.yedu.backend.admin.application.usecase.AdminInfoUseCase;
import com.yedu.backend.admin.application.usecase.AdminManageUseCase;
import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminInfoUseCase adminInfoUseCase;
    private final AdminManageUseCase adminManageUseCase;

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
            @RequestParam(required = false) List<District> districts,
            @RequestParam(required = false) List<ClassType> subjects,
            @RequestParam(required = false) List<String> universities,
            @RequestParam(required = false) List<TeacherGender> genders,
            @RequestParam(required = false) String search) {
        TeacherSearchRequest request = new TeacherSearchRequest(districts, subjects, universities, genders, search);
        AllFilteringTeacher allFilteringTeacher = adminInfoUseCase.searchAllTeacher(request);
        return ResponseEntity.ok(allFilteringTeacher);
    }
}
