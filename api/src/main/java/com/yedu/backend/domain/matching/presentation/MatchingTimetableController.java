package com.yedu.backend.domain.matching.presentation;

import com.yedu.backend.domain.matching.application.dto.req.MatchingTimeTableRequest;
import com.yedu.backend.domain.matching.application.dto.req.MatchingTimeTableRetrieveRequest;
import com.yedu.backend.domain.matching.application.dto.res.MatchingTimetableRetrieveResponse;
import com.yedu.backend.domain.matching.application.usecase.MatchingTimetableUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matching/timetable")
@RequiredArgsConstructor
@Tag(name = "MATCHING_TIMETABLE Controller", description = "학부모->선생님 프로필 확인 후 수업시간 확정시 사용할 API")
public class MatchingTimetableController {
    private final MatchingTimetableUseCase matchingTimeTableUseCase;

    @GetMapping
    @Operation(summary = "과외 매칭 시간 조회 API")
    public ResponseEntity<MatchingTimetableRetrieveResponse> retrieveMatchingTimetable(@ParameterObject MatchingTimeTableRetrieveRequest request) {
        MatchingTimetableRetrieveResponse response = matchingTimeTableUseCase.retrieveMatchingTimetable(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "학부모 과외 시간 설정 API")
    public ResponseEntity<Void> saveMatchingTimetable(@RequestBody MatchingTimeTableRequest request) {
        matchingTimeTableUseCase.matchingTimetable(request);
        return ResponseEntity.noContent().build();
    }
}
