package com.yedu.backend.domain.matching.application.dto.req;

import com.yedu.backend.domain.parents.domain.vo.DayTime;

import java.util.List;

public record MatchingTimeTableRequest(
        String classMatchingToken,
        List<DayTime> dayTimes
) {}
