package com.yedu.api.domain.matching.application.dto.req;

import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;
import java.util.List;

public record CompleteSessionRequest(
    String understanding,
    Integer homeworkPercentage
){}
