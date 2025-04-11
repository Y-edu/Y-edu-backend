package com.yedu.backend.domain.matching.application.dto.req;


public record ClassScheduleRetrieveRequest(
    String classScheduleManagementId,
    Long classMatchingId
) {

}
