package com.yedu.backend.domain.teacher.application.dto.res;

import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record TeacherAllInformationResponse(
    String profile,
    String nickName,
    List<String> districts,
    Map<Day, List<LocalTime>> available,
    String comment, // 소제목 역할 classInfo
    String introduce, // 자신을 어필하는 자기소개 classInfo
    int teachingHistory, // 경력 몇년
    List<String> teachingExperiences, // 영어 수업 경력
    List<String> foreignExperiences, // 해외 경험
    String university,
    String major,
    String highSchool,
    String teachingStyle1,
    String teachingStyleInfo1,
    String teachingStyle2,
    String teachingStyleInfo2,
    String teachingStyle,
    String video
) {
}
