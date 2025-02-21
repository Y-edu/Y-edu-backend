package com.yedu.backend.domain.teacher.application.dto.req;

import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;

import java.util.List;

public record TeacherInfoFormRequest(
        String name,
        String nickName,
        String email,
        String phoneNumber,
        String birth,
        TeacherGender gender,
        String university,
        String major,
        String highSchool,
        String introduce,
        String teachingStyle1,
        String teachingStyleInfo1,
        String teachingStyle2,
        String teachingStyleInfo2,
        boolean englishPossible,
        boolean mathPossible,
        String comment,
        List<List<String>> available,
        List<String> region,
        String source,
        boolean marketingAgree,
        English english,
        Math math
) {
    public record English(
            String teachingExperience,
            String foreignExperience,
            String teachingHistory,
            String teachingStyle
    ) {}

    public record Math(
            String teachingExperience,
            String teachingHistory,
            String teachingStyle
    ) {}
}