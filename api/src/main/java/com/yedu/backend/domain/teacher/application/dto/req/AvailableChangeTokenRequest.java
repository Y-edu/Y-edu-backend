package com.yedu.backend.domain.teacher.application.dto.req;

import com.yedu.backend.domain.parents.domain.vo.DayTime;
import java.util.List;

public record AvailableChangeTokenRequest(String token, List<DayTime> dayTimes) {}
