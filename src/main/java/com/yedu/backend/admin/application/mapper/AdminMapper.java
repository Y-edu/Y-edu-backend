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
                applicationForm.getPay(),
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
        ApplicationForm applicationForm = classMatching.getApplicationForm();
        String refuseReason = Optional.ofNullable(classMatching.getRefuseReason()).orElse(null);
        return new AlarmTalkResponse(
                classMatching.getClassMatchingId(),
                teacher.getTeacherId(),
                applicationForm.getWantedSubject(),
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
        return new ClassDetailsResponse(
                applicationForm.getClassCount(),
                applicationForm.getClassTime(),
                applicationForm.getPay(),
                applicationForm.getAge(),
                applicationForm.getWantTime(),
                applicationForm.getWantedSubject(),
                applicationForm.getOnline(),
                applicationForm.getFavoriteGender(),
                applicationForm.getDistrict(),
                applicationForm.getDong(),
                goals,
                applicationForm.getFavoriteStyle(),
                applicationForm.getSource()
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
                teacher.getTeacherSchoolInfo().getMajor(),
                districts,
                teacherInfo.getVideo(),
                teacher.getIssue()
        );
    }
}
