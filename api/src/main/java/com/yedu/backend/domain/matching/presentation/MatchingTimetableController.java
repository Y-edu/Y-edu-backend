package com.yedu.backend.domain.matching.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matching/timetable")
@RequiredArgsConstructor
@Tag(name = "MATCHING_TIMETABLE Controller")
public class MatchingTimetableController {

}
