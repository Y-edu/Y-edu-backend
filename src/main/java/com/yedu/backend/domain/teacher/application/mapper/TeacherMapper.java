package com.yedu.backend.domain.teacher.application.mapper;

import com.yedu.backend.domain.teacher.application.dto.req.TeacherInfoFormRequest;
import com.yedu.backend.domain.teacher.domain.entity.*;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeachingStyle;

import java.time.LocalTime;

public class TeacherMapper {
    public static TeacherInfo mapToTeacherInfo(TeacherInfoFormRequest request) {
        TeacherInfo.TeacherInfoBuilder builder = TeacherInfo.builder()
                .name(request.name())
                .nickName(request.nickName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .birth(request.birth())
                .gender(false);
        if (request.gender().equals("남"))
            builder.gender(true);
        return builder.build();
    }

    public static TeacherSchoolInfo mapToTeacherSchoolInfo(TeacherInfoFormRequest request) {
        return TeacherSchoolInfo.builder()
                .university(request.univercity())
                .major(request.major())
                .highSchool(request.highSchool())
                .highSchoolType(request.highSchoolType())
                .build();
    }

    public static TeacherClassInfo mapToTeacherClassInfo(TeacherInfoFormRequest request) {
        // style enum으로 사용 예정
        TeacherClassInfo.TeacherClassInfoBuilder builder = TeacherClassInfo.builder()
                .history(Integer.parseInt(request.history()))
                .introduce(request.introduce())
                .teachingStyle1(TeachingStyle.fromString(request.teachingStyle1()))
                .teachingStyle2(TeachingStyle.fromString(request.teachingStyle2()))
                .teachingStyleInfo1(request.teachingStyleInfo1())
                .teachingStyleInfo2(request.teachingStyleInfo1())
                .recommendStudent(request.recommenedStudent())
                .comment(request.comment())
                .englishPossible(false)
                .mathPossible(false);
        if (request.englishPossible())
            builder.englishPossible(true);
        if (request.mathPossible())
            builder.englishPossible(true);
        return builder.build();
    }

    public static Teacher mapToTeacher(TeacherInfoFormRequest request) {
        return Teacher.builder()
                .teacherInfo(mapToTeacherInfo(request))
                .teacherSchoolInfo(mapToTeacherSchoolInfo(request))
                .teacherClassInfo(mapToTeacherClassInfo(request))
                .source(request.source())
                .marketingAgree(request.marketingAgree())
                .build();
    }

    public static TeacherDistrict mapToTeacherDistrict(Teacher teacher, String region) {
        return TeacherDistrict.builder()
                .district(District.fromString(region))
                .teacher(teacher)
                .build();
    }

    public static TeacherAvailable mapToTeacherAvailable(Teacher teacher, int day, String time) {
        return TeacherAvailable.builder()
                .teacher(teacher)
                .day(Day.byInt(day))
                .availableTime(LocalTime.parse(time))
                .build();
    }

    public static TeacherEnglish mapToTeacherEnglish(Teacher teacher, TeacherInfoFormRequest request) {
        return TeacherEnglish.builder()
                .teacher(teacher)
                .appealPoint(request.english().appealPoint())
                .teachingExperience(request.english().teachingExperience())
                .teachingStyle(request.english().teachingStyle())
                .managementStyle(request.english().managementStyle())
                .foreignExperience(request.english().foreignExperience())
                .build();
    }

    public static TeacherMath mapToTeacherMath(Teacher teacher, TeacherInfoFormRequest request) {
        return TeacherMath.builder()
                .teacher(teacher)
                .appealPoint(request.math().appealPoint())
                .teachingExperience(request.math().teachingExperience())
                .teachingStyle(request.math().teachingExperience())
                .teachingStyle(request.math().teachingStyle())
                .managementStyle(request.math().managementStyle())
                .build();
    }
}
