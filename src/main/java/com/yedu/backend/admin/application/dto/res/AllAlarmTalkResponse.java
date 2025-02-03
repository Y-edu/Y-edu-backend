package com.yedu.backend.admin.application.dto.res;

import com.yedu.backend.domain.matching.domain.entity.constant.MatchingStatus;

import java.util.List;

public record AllAlarmTalkResponse(
        int accept,
        int total,
        int time,
        List<AlarmTalkResponse> alarmTalkResponses
) {
    public record AlarmTalkResponse(
            long classMatchingId,
            MatchingStatus status,
            String nickName,
            String name,
            int responseTime,
            int accept,
            int total,
            String refuseReason
    ) {}
}
