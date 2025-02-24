package com.yedu.backend.domain.teacher.application.mapper;

import com.yedu.backend.domain.teacher.application.dto.req.TeacherInfoFormRequest;
import com.yedu.backend.domain.teacher.application.dto.res.*;
import com.yedu.backend.domain.teacher.domain.entity.*;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeachingStyle;
import com.yedu.backend.domain.teacher.domain.entity.constant.University;
import com.yedu.backend.global.excel.application.dto.TeacherInfoRequest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TeacherMapper {

    private static TeacherInfo mapToTeacherInfo(TeacherInfoFormRequest request) {
        return TeacherInfo.builder()
                .name(request.name())
                .nickName(request.nickName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .birth(request.birth())
                .gender(request.gender())
                .build();
    }

    private static TeacherSchoolInfo mapToTeacherSchoolInfo(TeacherInfoFormRequest request) {
        boolean etc = University.checkEtc(request.university());
        return TeacherSchoolInfo.builder()
                .university(request.university())
                .etc(etc)
                .major(request.major())
                .highSchool(request.highSchool())
                .build();
    }

    private static TeacherClassInfo mapToTeacherClassInfo(TeacherInfoFormRequest request) {
        // style enum으로 사용 예정
        TeacherClassInfo.TeacherClassInfoBuilder builder = TeacherClassInfo.builder()
                .introduce(request.introduce())
                .teachingStyle1(TeachingStyle.fromString(request.teachingStyle1()))
                .teachingStyle2(TeachingStyle.fromString(request.teachingStyle2()))
                .teachingStyleInfo1(request.teachingStyleInfo1())
                .teachingStyleInfo2(request.teachingStyleInfo2())
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
                .teachingExperience(request.english().teachingExperience())
                .teachingHistory(Integer.parseInt(request.english().teachingHistory()))
                .teachingStyle(request.english().teachingStyle())
                .foreignExperience(request.english().foreignExperience())
                .build();
    }

    public static TeacherMath mapToTeacherMath(Teacher teacher, TeacherInfoFormRequest request) {
        return TeacherMath.builder()
                .teacher(teacher)
                .teachingExperience(request.math().teachingExperience())
                .teachingHistory(Integer.parseInt(request.math().teachingHistory()))
                .teachingStyle(request.math().teachingStyle())
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
        List<String> experiences = getOrganizeContexts(math.getTeachingExperience());

        return new TeacherMathResponse(
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
                classInfo.getTeachingStyleInfo2()
        );
    }

    public static TeacherEnglishResponse mapToTeacherEnglish(Teacher teacher, TeacherEnglish english) {
        TeacherClassInfo classInfo = teacher.getTeacherClassInfo();
        TeacherSchoolInfo schoolInfo = teacher.getTeacherSchoolInfo();
        List<String> foreignExperiences = getForeignExperiences(english);
        List<String> experiences = getOrganizeContexts(english.getTeachingExperience());
        return new TeacherEnglishResponse(
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
                classInfo.getTeachingStyleInfo2()
        );
    }

    private static List<String> getOrganizeContexts(String context) {
        if (context == null)
            return new ArrayList<>();
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
        return new MathCurriculumResponse(math.getTeachingStyle());
    }

    public static EnglishCurriculumResponse mapToEnglishCurriculumResponse(TeacherInfo teacherInfo, TeacherEnglish english) {
        return new EnglishCurriculumResponse(english.getTeachingStyle(), teacherInfo.getVideo());
    }

    public static DistrictAndTimeResponse mapToDistrictAndTimeResponse(List<String> districts, Map<Day, List<LocalTime>> availableTimes) {
        return new DistrictAndTimeResponse(districts, availableTimes);
    }

    private static TeacherInfo mapToTeacherInfo(TeacherInfoRequest request) {
        return TeacherInfo.builder()
                .name(request.name())
                .nickName(request.nickName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .birth(request.birth())
                .gender(request.gender())
                .build();
    }

    private static TeacherSchoolInfo mapToTeacherSchoolInfo(TeacherInfoRequest request) {
        boolean etc = University.checkEtc(request.university());
        return TeacherSchoolInfo.builder()
                .university(request.university())
                .etc(etc)
                .major(request.major())
                .highSchool(request.highSchool())
                .build();
    }

    private static TeacherClassInfo mapToTeacherClassInfo(TeacherInfoRequest request) {
        // style enum으로 사용 예정
        TeacherClassInfo.TeacherClassInfoBuilder builder = TeacherClassInfo.builder()
                .introduce(request.introduce())
                .teachingStyle1(TeachingStyle.fromString(request.teachingStyle1()))
                .teachingStyle2(TeachingStyle.fromString(request.teachingStyle2()))
                .teachingStyleInfo1(request.teachingStyleInfo1())
                .teachingStyleInfo2(request.teachingStyleInfo2())
                .comment(request.comment())
                .englishPossible(request.englishPossible())
                .mathPossible(request.mathPossible());
        return builder.build();
    }

    public static Teacher mapToTeacher(TeacherInfoRequest request) {
        return Teacher.builder()
                .teacherInfo(mapToTeacherInfo(request))
                .teacherSchoolInfo(mapToTeacherSchoolInfo(request))
                .teacherClassInfo(mapToTeacherClassInfo(request))
                .source(request.source())
                .marketingAgree(request.marketingAgree())
                .build();
    }

    public static TeacherEnglish mapToTeacherEnglish(Teacher teacher, TeacherInfoRequest request) {
        return TeacherEnglish.builder()
                .teacher(teacher)
                .teachingExperience(request.english().teachingExperience())
                .teachingHistory(request.english().teachingHistory())
                .teachingStyle(request.english().teachingStyle())
                .foreignExperience(request.english().foreignExperience())
                .build();
    }

    public static TeacherMath mapToTeacherMath(Teacher teacher, TeacherInfoRequest request) {
        return TeacherMath.builder()
                .teacher(teacher)
                .teachingExperience(request.math().teachingExperience())
                .teachingHistory(request.math().teachingHistory())
                .teachingStyle(request.math().teachingStyle())
                .build();
    }

}
