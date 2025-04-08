package com.yedu.backend.global.event.dto;

import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.parents.domain.entity.constant.Online;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;

public record NotifyClassInfoEvent(
        Online online,
        String applicationFormId,
        String nickName,
        ClassType classType,
        District district,
        String dong,
        long teacherId,
        String phoneNumber
) {
}
