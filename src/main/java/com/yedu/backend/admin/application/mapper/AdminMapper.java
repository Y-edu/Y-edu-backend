package com.yedu.backend.admin.application.mapper;

import com.yedu.backend.admin.application.dto.res.AllAlarmTalkResponse.AlarmTalkResponse;
import com.yedu.backend.admin.application.dto.res.AllApplicationResponse.ApplicationResponse;
import com.yedu.backend.admin.application.dto.res.AllFilteringTeacher.FilteringTeacher;
import com.yedu.backend.admin.application.dto.res.ClassDetailsResponse;
import com.yedu.backend.admin.application.dto.res.CommonParentsResponse;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherClassInfo;
import com.yedu.backend.domain.teacher.domain.entity.TeacherInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminMapper {
    public static ApplicationResponse mapToApplicationResponse(ApplicationForm applicationForm, int accept, int total) {
        Parents parents = applicationForm.getParents();
        String kakaoName = Optional.ofNullable(parents.getKakaoName()).orElse(null);
        return new ApplicationResponse(
                applicationForm.getApplicationFormId(),
                kakaoName,
                applicationForm.getClassCount(),
                applicationForm.getClassTime(),
                applicationForm.getWantedSubject().name(),
                applicationForm.getSource(),
                applicationForm.getCreatedAt().toString(),
                accept,
                total,
                parents.getPhoneNumber(),
                applicationForm.isProceedStatus()
        );
    }

    public static CommonParentsResponse mapToCommonParentsResponse(ApplicationForm applicationForm) {
        Parents parents = applicationForm.getParents();
        String kakaoName = Optional.ofNullable(parents.getKakaoName()).orElse(null);
        return new CommonParentsResponse(
                parents.getParentsId(),
                applicationForm.getApplicationFormId(),
                applicationForm.getWantedSubject().name(),
                kakaoName,
                parents.getPhoneNumber()
        );
    }

    public static AlarmTalkResponse mapToAlarmTalkResponse(ClassMatching classMatching) {
        Teacher teacher = classMatching.getTeacher();
        String refuseReason = Optional.ofNullable(classMatching.getRefuseReason()).orElse(null);
        return new AlarmTalkResponse(
                classMatching.getClassMatchingId(),
                classMatching.getMatchStatus(),
                teacher.getTeacherInfo().getNickName(),
                teacher.getTeacherInfo().getName(),
                0,
                0,
                0,
                refuseReason
        );
    }

    public static ClassDetailsResponse mapToClassDetailsResponse(ApplicationForm applicationForm, List<String> goals) {
        // 가격 계산식 = 4주 기준 분 * 600
        String count = applicationForm.getClassCount();
        int classCount = 0;
        if (count.equals("주 1회"))
            classCount = 1;
        else if (count.equals("주 2회"))
            classCount = 2;
        else if (count.equals("주 3회"))
            classCount = 3;
        else if (count.equals("주 4회"))
            classCount = 4;
        String time = applicationForm.getClassTime();
        int classTime = 0;
        if (time.equals("50분"))
            classTime = 50;
        if (time.equals("60분"))
            classTime = 60;
        if (time.equals("75분"))
            classTime = 75;
        if (time.equals("100분"))
            classTime = 100;
        if (time.equals("120분"))
            classTime = 120;
        return new ClassDetailsResponse(
                classCount,
                classTime,
                classTime*classCount*4*600,
                applicationForm.getAge(),
                applicationForm.getWantedSubject(),
                applicationForm.getOnline(),
                applicationForm.getFavoriteGender(),
                applicationForm.getDistrict(),
                applicationForm.getDong(),
                goals,
                applicationForm.getFavoriteStyle()
        );
    }

    public static FilteringTeacher mapToAllFilteringTeacherResponse(Teacher teacher, List<String> districts) {
        TeacherInfo teacherInfo = teacher.getTeacherInfo();
        TeacherClassInfo classInfo = teacher.getTeacherClassInfo();
        List<ClassType> classTypes = new ArrayList<>();
        if (classInfo.isEnglishPossible())
            classTypes.add(ClassType.영어);
        if (classInfo.isMathPossible())
            classTypes.add(ClassType.수학);

        return new FilteringTeacher(
                teacher.getTeacherId(),
                teacherInfo.getNickName(),
                classTypes,
                teacherInfo.getName(),
                teacher.getStatus(),
                0,
                0,
                teacher.getTeacherSchoolInfo().getUniversity(),
                districts,
                teacher.getIssue()
        );
    }
}
