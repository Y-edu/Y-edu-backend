package com.yedu.api.domain.matching.application.dto.req;

import com.yedu.api.domain.parents.domain.vo.DayTime;
import java.util.List;

public record MatchingTimeTableRequest(String classMatchingToken, List<DayTime> dayTimes) {}
