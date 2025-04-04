package com.yedu.backend.domain.matching.presentation;

import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleConfirmRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleMatchingResponse;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleRefuseRequest;
import com.yedu.backend.domain.matching.application.usecase.ClassScheduleMatchingUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matching/schedule")
@RequiredArgsConstructor
@Tag(name = "CLASS_MATCHING_SCHEDULE Controller")
public class ClassMatchingScheduleController {

    private final ClassScheduleMatchingUseCase scheduleMatchingUseCase;


    @PostMapping
    @Operation(summary = "선생님과 학부모의 일정 조율 매칭 신청 API")
    public ClassScheduleMatchingResponse requestScheduleMatch(@RequestBody ClassScheduleMatchingRequest request) {
        Long managementId = scheduleMatchingUseCase.schedule(request);

        return new ClassScheduleMatchingResponse(managementId);
    }

    @DeleteMapping
    @Operation(summary = "매칭 거절 API")
    public void refuseScheduleMatch(@RequestBody ClassScheduleRefuseRequest request) {
        scheduleMatchingUseCase.refuse(request);
    }

    @PutMapping
    @Operation(summary = "매칭 확정 API")
    public void confirmScheduleMatch(@RequestBody ClassScheduleConfirmRequest request) {
        scheduleMatchingUseCase.confirm(request);
    }
}
