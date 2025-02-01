package com.yedu.backend.domain.teacher.application.dto.res;

public record TeacherMathResponse(
        String appealPoint, // 수학 전문성 math
        String comment, // 소제목 역할 classInfo
        String introduce, // 자신을 어필하는 자기소개 classInfo
        int teachingHistory, // 경력 몇년
        String teachingExperience, // 수학 수업 경력
        String university,
        String major,
        String highSchool,
        String teachingStyle1,
        String teachingStyleInfo1,
        String teachingStyle2,
        String teachingStyleInfo2,
        String recommendStudent
) {
}
