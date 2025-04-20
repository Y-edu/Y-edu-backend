package com.yedu.backend.global.excel.application.dto;

import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;
import java.util.List;

public record TeacherInfoRequest(
    String name,
    String nickName,
    String email,
    String phoneNumber,
    String birth,
    TeacherGender gender,
    String university,
    String major,
    String highSchool,
    String highSchoolType,
    String introduce,
    String teachingStyle1,
    String teachingStyleInfo1,
    String teachingStyle2,
    String teachingStyleInfo2,
    boolean englishPossible,
    boolean mathPossible,
    String recommenedStudent,
    String comment,
    List<List<String>> available,
    List<String> region,
    String source,
    boolean marketingAgree,
    English english,
    Math math) {
  public record English(
      String appealPoint,
      String teachingExperience,
      String foreignExperience,
      int teachingHistory,
      String teachingStyle,
      String managementStyle) {}

  public record Math(
      String appealPoint,
      String teachingExperience,
      int teachingHistory,
      String teachingStyle,
      String managementStyle) {}
}
