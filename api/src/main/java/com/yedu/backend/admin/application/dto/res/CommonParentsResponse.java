package com.yedu.backend.admin.application.dto.res;

public record CommonParentsResponse(
    Long parentsId,
    String applicationFormId,
    String subject,
    String kakaoName,
    String phoneNumber) {}
