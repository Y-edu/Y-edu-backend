package com.yedu.backend.domain.matching.presentation;

import com.yedu.backend.domain.matching.application.usecase.ClassMatchingInfoUseCase;
import com.yedu.backend.domain.matching.application.dto.res.ApplicationFormToTeacherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
public class ClassMatchingController {
    private final ClassMatchingInfoUseCase matchingInfoUseCase;

    @GetMapping("/application/{applicationFormId}/{teacherId}")
    public ResponseEntity<ApplicationFormToTeacherResponse> applicationToTeacher(@PathVariable String applicationFormId, @PathVariable long teacherId) {
        ApplicationFormToTeacherResponse applicationFormToTeacherResponse = matchingInfoUseCase.applicationFormToTeacher(applicationFormId, teacherId);
        return ResponseEntity.ok(applicationFormToTeacherResponse);
    }
}
