package com.yedu.backend.admin.application.dto.res;

import java.util.List;

public record AllApplicationResponse(
        List<ApplicationResponse> applicationResponses
) {
    public record ApplicationResponse(String applicationFormId,
                                      String kakaoName,
                                      String classCount,
                                      String classTime,
                                      String wantedSubject,
                                      String source,
                                      String createdAt,
                                      int accept,
                                      int total,
                                      String phoneNumber,
                                      boolean status){}
}
