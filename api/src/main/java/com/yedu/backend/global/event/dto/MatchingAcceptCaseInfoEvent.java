package com.yedu.backend.global.event.dto;

import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.parents.domain.entity.constant.Online;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;

public record MatchingAcceptCaseInfoEvent(
        Online online,
        ClassType classType,
        District district,
        String dong,
        String age,
        String phoneNumber
) {
}
