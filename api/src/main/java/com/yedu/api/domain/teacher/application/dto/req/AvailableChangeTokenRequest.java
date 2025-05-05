package com.yedu.api.domain.teacher.application.dto.req;

import com.yedu.api.domain.parents.domain.vo.DayTime;
import java.util.List;

public record AvailableChangeTokenRequest(String token, List<DayTime> dayTimes) {}
