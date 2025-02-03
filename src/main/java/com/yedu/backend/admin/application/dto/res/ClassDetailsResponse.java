package com.yedu.backend.admin.application.dto.res;

import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.parents.domain.entity.constant.Gender;
import com.yedu.backend.domain.parents.domain.entity.constant.Online;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;

public record ClassDetailsResponse(
        int classCount,
        int classTime,
        int pay,
        String age,
        ClassType wantedSubject,
        Online online,
        Gender favoriteGender,
        District district,
        String dong
) {
}
