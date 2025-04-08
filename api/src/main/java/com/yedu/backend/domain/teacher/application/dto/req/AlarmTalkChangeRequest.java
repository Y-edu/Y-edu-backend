package com.yedu.backend.domain.teacher.application.dto.req;

public record AlarmTalkChangeRequest(
        String name,
        String phoneNumber,
        boolean alarmTalk
) {
}
