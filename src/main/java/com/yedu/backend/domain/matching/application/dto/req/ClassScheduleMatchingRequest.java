package com.yedu.backend.domain.matching.application.dto.req;

public record ClassScheduleMatchingRequest(
    String applicationFormId,
    Long teacherId,
    String phoneNumber
) {

}
