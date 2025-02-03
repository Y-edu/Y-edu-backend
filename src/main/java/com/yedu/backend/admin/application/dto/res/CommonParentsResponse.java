package com.yedu.backend.admin.application.dto.res;

public record CommonParentsResponse(
        Long parentsId,
        String parentsCode,
        String kakaoName,
        String phoneNumber
) {
}
