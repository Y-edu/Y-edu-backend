package com.yedu.backend.domain.matching.presentation;

import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleConfirmRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleMatchingResponse;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleRefuseRequest;
import com.yedu.backend.domain.matching.application.usecase.ClassScheduleMatchingUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matching/schedule")
@RequiredArgsConstructor
@Tag(name = "CLASS_MATCHING_SCHEDULE Controller", description = "매칭 후반부 자동화 스프린트에 사용될 API")
public class ClassMatchingScheduleController {

    private final ClassScheduleMatchingUseCase scheduleMatchingUseCase;


    @PostMapping
    @Operation(summary = "상담 신행 API")
    public ResponseEntity<ClassScheduleMatchingResponse> requestScheduleMatch(@RequestBody ClassScheduleMatchingRequest request) {
        String key = scheduleMatchingUseCase.schedule(request);

        return ResponseEntity.ok(new ClassScheduleMatchingResponse(key));
    }

    @DeleteMapping
    @Operation(summary = "상담 후 미진행 API")
    public ResponseEntity<Void> refuseScheduleMatch(@RequestBody ClassScheduleRefuseRequest request) {
        scheduleMatchingUseCase.refuse(request);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Operation(summary = "상담 후 진행 API")
    public ResponseEntity<Void> confirmScheduleMatch(@RequestBody ClassScheduleConfirmRequest request) {
        scheduleMatchingUseCase.confirm(request);

        return ResponseEntity.noContent().build();
    }
}
