package com.yedu.backend.domain.matching.application.dto.req;

public record ClassScheduleRefuseRequest(
    Long classScheduleManagementId,
    String refuseReason
) {

}
