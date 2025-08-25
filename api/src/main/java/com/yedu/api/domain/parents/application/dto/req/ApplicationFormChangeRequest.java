package com.yedu.api.domain.parents.application.dto.req;

import com.yedu.api.domain.parents.domain.entity.constant.Gender;


public record ApplicationFormChangeRequest(
    String phoneNumber,
    String wantedTime,
    Gender favoriteGender,
    String favoriteStyle,
    Boolean useSameClassCount,
    String classCount,
    String classTime,
    String dong
) {}
