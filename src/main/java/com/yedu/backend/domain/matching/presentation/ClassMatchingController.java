package com.yedu.backend.domain.matching.presentation;

import com.yedu.backend.domain.matching.application.dto.req.ClassMatchingRefuseRequest;
import com.yedu.backend.domain.matching.application.usecase.ClassMatchingInfoUseCase;
import com.yedu.backend.domain.matching.application.dto.res.ClassMatchingForTeacherResponse;
import com.yedu.backend.domain.matching.application.usecase.ClassMatchingManageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
public class ClassMatchingController {
    private final ClassMatchingInfoUseCase matchingInfoUseCase;
    private final ClassMatchingManageUseCase matchingManageUseCase;

    @GetMapping("/application/{applicationFormId}/{teacherId}/{phoneNumber}")
    public ResponseEntity<ClassMatchingForTeacherResponse> applicationToTeacher(@PathVariable String applicationFormId, @PathVariable long teacherId, @PathVariable String phoneNumber) {
        ClassMatchingForTeacherResponse classMatchingForTeacherResponse = matchingInfoUseCase.applicationFormToTeacher(applicationFormId, teacherId, phoneNumber);
        return ResponseEntity.ok(classMatchingForTeacherResponse);
    }

    @PutMapping("/application/refuse/{applicationFormId}/{teacherId}/{phoneNumber}")
    public ResponseEntity refuseApplication(@PathVariable String applicationFormId, @PathVariable long teacherId, @PathVariable String phoneNumber, @RequestBody ClassMatchingRefuseRequest request) {
        matchingManageUseCase.refuseClassMatching(applicationFormId, teacherId, phoneNumber, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/application/accept/{applicationFormId}/{teacherId}/{phoneNumber}")
    public ResponseEntity acceptApplication(@PathVariable String applicationFormId, @PathVariable long teacherId, @PathVariable String phoneNumber) {
        matchingManageUseCase.acceptClassMatching(applicationFormId, teacherId, phoneNumber);
        return ResponseEntity.ok().build();
    }
}
