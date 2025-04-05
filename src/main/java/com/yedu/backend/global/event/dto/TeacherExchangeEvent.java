package com.yedu.backend.global.event.dto;

public record TeacherExchangeEvent(
        String applicationFormId,
        String classCount,
        String time,
        String age,
        String district,
        int money,
        String parentsPhoneNumber,
        String teacherPhoneNumber,
        long managementId
) {
}
