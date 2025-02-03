package com.yedu.backend.admin.presentation;

import com.yedu.backend.admin.application.dto.res.AllAlarmTalkResponse;
import com.yedu.backend.admin.application.dto.res.AllApplicationResponse;
import com.yedu.backend.admin.application.dto.res.ClassDetailsResponse;
import com.yedu.backend.admin.application.dto.res.CommonParentsResponse;
import com.yedu.backend.admin.application.usecase.AdminInfoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminInfoUseCase adminInfoUseCase;

    @GetMapping("/all/matching")
    public ResponseEntity<AllApplicationResponse> allApplication() {
        AllApplicationResponse allApplication = adminInfoUseCase.getAllApplication();
        return ResponseEntity.ok(allApplication);
    }

//    @PutMapping("/all/matching/{applicationFormId}")
//    public ResponseEntity updateProceed(@PathVariable String applicationFormId) {
//
//    }

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
}
