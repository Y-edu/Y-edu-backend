package com.yedu.backend.domain.teacher.application.dto.res;

import java.util.List;

public record TeacherMathResponse(
        String comment, // 소제목 역할 classInfo
        String introduce, // 자신을 어필하는 자기소개 classInfo
        int teachingHistory, // 경력 몇년
        List<String> teachingExperiences, // 수학 수업 경력
        String university,
        String major,
        String highSchool,
        String teachingStyle1,
        String teachingStyleInfo1,
        String teachingStyle2,
        String teachingStyleInfo2
) {
}
