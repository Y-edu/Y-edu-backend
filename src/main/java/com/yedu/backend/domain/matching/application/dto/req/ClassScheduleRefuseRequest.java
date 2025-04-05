package com.yedu.backend.domain.matching.application.dto.req;

public record ClassScheduleRefuseRequest(
    String classScheduleManagementId,
    String refuseReason
) {

}
