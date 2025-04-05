package com.yedu.backend.global.event.dto;

public record TeacherClassRemindEvent(
        String nickName,
        String phoneNumber,
        long managementId
) {
}
