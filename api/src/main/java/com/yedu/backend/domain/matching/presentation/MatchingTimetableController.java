package com.yedu.backend.domain.matching.presentation;

import com.yedu.backend.domain.matching.application.dto.req.MatchingTimeTableRetrieveRequest;
import com.yedu.backend.domain.matching.application.dto.res.MatchingTimetableRetrieveResponse;
import com.yedu.backend.domain.matching.application.usecase.MatchingTimetableUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matching/timetable")
@RequiredArgsConstructor
@Tag(name = "MATCHING_TIMETABLE Controller")
public class MatchingTimetableController {
    private final MatchingTimetableUseCase matchingTimeTableUseCase;

    @GetMapping()
    public ResponseEntity<MatchingTimetableRetrieveResponse> matchingTimeTable(@ParameterObject MatchingTimeTableRetrieveRequest request) {
        MatchingTimetableRetrieveResponse response = matchingTimeTableUseCase.retrieveMatchingTimetable(request);
        return ResponseEntity.ok(response);
    }
}
