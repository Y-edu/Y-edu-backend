package com.yedu.backend.domain.teacher.application.mapper;

import com.yedu.backend.domain.teacher.application.dto.req.TeacherInfoFormRequest;
import com.yedu.backend.domain.teacher.application.dto.res.*;
import com.yedu.backend.domain.teacher.domain.entity.*;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeachingStyle;
import com.yedu.backend.domain.teacher.domain.entity.constant.University;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TeacherMapper {
    public static TeacherInfo mapToTeacherInfo(TeacherInfoFormRequest request) {
        return TeacherInfo.builder()
                .name(request.name())
                .nickName(request.nickName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .birth(request.birth())
                .gender(request.gender())
                .build();
    }

    public static TeacherSchoolInfo mapToTeacherSchoolInfo(TeacherInfoFormRequest request) {
        boolean etc = University.checkEtc(request.univercity());
        return TeacherSchoolInfo.builder()
                .university(request.univercity())
                .etc(etc)
                .major(request.major())
                .highSchool(request.highSchool())
                .highSchoolType(request.highSchoolType())
                .build();
    }

    public static TeacherClassInfo mapToTeacherClassInfo(TeacherInfoFormRequest request) {
        // style enum으로 사용 예정
        TeacherClassInfo.TeacherClassInfoBuilder builder = TeacherClassInfo.builder()
                .introduce(request.introduce())
                .teachingStyle1(TeachingStyle.fromString(request.teachingStyle1()))
                .teachingStyle2(TeachingStyle.fromString(request.teachingStyle2()))
                .teachingStyleInfo1(request.teachingStyleInfo1())
                .teachingStyleInfo2(request.teachingStyleInfo1())
                .recommendStudent(request.recommenedStudent())
                .comment(request.comment())
                .englishPossible(request.englishPossible())
                .mathPossible(request.mathPossible());
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
                .teachingHistory(Integer.parseInt(request.english().teachingHistory()))
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
                .teachingHistory(Integer.parseInt(request.math().teachingHistory()))
                .teachingStyle(request.math().teachingStyle())
                .managementStyle(request.math().managementStyle())
                .build();
    }

    public static TeacherCommonsInfoResponse mapToTeacherCommonsInfo(Teacher teacher) {
        TeacherInfo info = teacher.getTeacherInfo();
        return new TeacherCommonsInfoResponse(
                info.getProfile(),
                info.getNickName()
        );
    }

    public static TeacherMathResponse mapToTeacherMathResponse(Teacher teacher, TeacherMath math) {
        TeacherClassInfo classInfo = teacher.getTeacherClassInfo();
        TeacherSchoolInfo schoolInfo = teacher.getTeacherSchoolInfo();
        List<String> appealPoints = getOrganizeContexts(math.getAppealPoint());
        List<String> experiences = getOrganizeContexts(math.getTeachingExperience());
        List<String> recommends = getOrganizeContexts(classInfo.getRecommendStudent());

        return new TeacherMathResponse(
                appealPoints,
                classInfo.getComment(),
                classInfo.getIntroduce(),
                math.getTeachingHistory(),
                experiences,
                schoolInfo.getUniversity(),
                schoolInfo.getMajor(),
                schoolInfo.getHighSchool(),
                classInfo.getTeachingStyle1().getDescription(),
                classInfo.getTeachingStyleInfo1(),
                classInfo.getTeachingStyle2().getDescription(),
                classInfo.getTeachingStyleInfo2(),
                recommends
        );
    }

    public static TeacherEnglishResponse mapToTeacherEnglish(Teacher teacher, TeacherEnglish english) {
        TeacherClassInfo classInfo = teacher.getTeacherClassInfo();
        TeacherSchoolInfo schoolInfo = teacher.getTeacherSchoolInfo();
        List<String> appealPoints = getOrganizeContexts(english.getAppealPoint());
        List<String> foreignExperiences = getForeignExperiences(english);
        List<String> experiences = getOrganizeContexts(english.getTeachingExperience());
        List<String> recommends = getOrganizeContexts(classInfo.getRecommendStudent());
        return new TeacherEnglishResponse(
                appealPoints,
                classInfo.getComment(),
                classInfo.getIntroduce(),
                english.getTeachingHistory(),
                experiences,
                foreignExperiences,
                schoolInfo.getUniversity(),
                schoolInfo.getMajor(),
                schoolInfo.getHighSchool(),
                classInfo.getTeachingStyle1().getDescription(),
                classInfo.getTeachingStyleInfo1(),
                classInfo.getTeachingStyle2().getDescription(),
                classInfo.getTeachingStyleInfo2(),
                recommends
        );
    }

    private static List<String> getOrganizeContexts(String context) {
        String organizeContext = context.replace("#", "");
        return Arrays.stream(organizeContext.split("\n")).toList()
                .stream()
                .map(String::trim)
                .toList();
    }

    private static List<String> getForeignExperiences(TeacherEnglish english) {
        List<String> foreignExperiences = new ArrayList<>();
        if (english.getForeignExperience() != null) {
            String foreignExperience = english.getForeignExperience().replace("#", "");
            foreignExperiences = Arrays.stream(foreignExperience.split("\n")).toList()
                    .stream()
                    .map(String::trim)
                    .toList();
        }
        return foreignExperiences;
    }

    public static MathCurriculumResponse mapToMathCurriculumResponse(TeacherMath math) {
        return new MathCurriculumResponse(math.getTeachingStyle(), math.getManagementStyle());
    }

    public static EnglishCurriculumResponse mapToEnglishCurriculumResponse(TeacherInfo teacherInfo, TeacherEnglish english) {
        return new EnglishCurriculumResponse(english.getTeachingStyle(), english.getManagementStyle(), teacherInfo.getVideo());
    }

    public static DistrictAndTimeResponse mapToDistrictAndTimeResponse(List<String> districts, Map<Day, List<LocalTime>> availableTimes) {
        return new DistrictAndTimeResponse(districts, availableTimes);
    }
}
