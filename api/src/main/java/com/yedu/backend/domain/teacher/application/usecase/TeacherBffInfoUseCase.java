package com.yedu.backend.domain.teacher.application.usecase;

import com.yedu.backend.domain.teacher.application.dto.res.DistrictAndTimeResponse;
import com.yedu.backend.domain.teacher.application.dto.res.EnglishCurriculumResponse;
import com.yedu.backend.domain.teacher.application.dto.res.MathCurriculumResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherAllInformationResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherCommonsInfoResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherEnglishResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherMathResponse;
import com.yedu.cache.support.dto.MatchingTimeTableDto;
import com.yedu.cache.support.storage.KeyStorage;
import com.yedu.common.type.ClassType;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class TeacherBffInfoUseCase {

  private final TeacherInfoUseCase teacherInfoUseCase;

  private final KeyStorage<MatchingTimeTableDto> matchingTimetableKeyStorage;

  public TeacherAllInformationResponse retrieveAllInformation(String token) {
    MatchingTimeTableDto matchingTimeTableDto = matchingTimetableKeyStorage.get(token);
    Long teacherId = matchingTimeTableDto.teacherId();
    ClassType classType = matchingTimeTableDto.classType();

    CompletableFuture<TeacherCommonsInfoResponse> commonsFuture =
        CompletableFuture.supplyAsync(() -> teacherInfoUseCase.teacherCommonsInfo(teacherId));

    CompletableFuture<DistrictAndTimeResponse> districtAndTimeFuture =
        CompletableFuture.supplyAsync(() -> teacherInfoUseCase.districtAndTime(teacherId));

    CompletableFuture<?> subjectInfoFuture = CompletableFuture.completedFuture(null);
    CompletableFuture<?> curriculumInfoFuture = CompletableFuture.completedFuture(null);

    if (classType.equals(ClassType.수학)) {
      subjectInfoFuture =
          CompletableFuture.supplyAsync(() -> teacherInfoUseCase.teacherMathDetails(teacherId));
      curriculumInfoFuture =
          CompletableFuture.supplyAsync(() -> teacherInfoUseCase.curriculumMath(teacherId));
    } else if (classType.equals(ClassType.영어)) {
      subjectInfoFuture =
          CompletableFuture.supplyAsync(() -> teacherInfoUseCase.teacherEnglishDetails(teacherId));
      curriculumInfoFuture =
          CompletableFuture.supplyAsync(() -> teacherInfoUseCase.curriculumEnglish(teacherId));
    }

    CompletableFuture.allOf(
            commonsFuture, districtAndTimeFuture, subjectInfoFuture, curriculumInfoFuture)
        .join();

    TeacherCommonsInfoResponse commonsInfoResponse = commonsFuture.join();
    DistrictAndTimeResponse districtAndTimeResponse = districtAndTimeFuture.join();
    Object subjectInfo = subjectInfoFuture.join();
    Object curriculumInfo = curriculumInfoFuture.join();

    TeacherAllInformationResponse.TeacherAllInformationResponseBuilder builder =
        TeacherAllInformationResponse.builder()
            .profile(commonsInfoResponse.profile())
            .nickName(commonsInfoResponse.nickName())
            .available(districtAndTimeResponse.availables())
            .districts(districtAndTimeResponse.districts());

    if (classType.equals(ClassType.수학)) {
      return buildMathTeacherInfo(
          builder, (TeacherMathResponse) subjectInfo, (MathCurriculumResponse) curriculumInfo);
    }

    if (classType.equals(ClassType.영어)) {
      return buildEnglishTeacherInfo(
          builder,
          (TeacherEnglishResponse) subjectInfo,
          (EnglishCurriculumResponse) curriculumInfo);
    }

    return builder.build();
  }

  private TeacherAllInformationResponse buildMathTeacherInfo(
      TeacherAllInformationResponse.TeacherAllInformationResponseBuilder builder,
      TeacherMathResponse mathResponse,
      MathCurriculumResponse mathCurriculumResponse) {
    return builder
        .subject(ClassType.수학)
        .comment(mathResponse.comment())
        .introduce(mathResponse.introduce())
        .teachingHistory(mathResponse.teachingHistory())
        .teachingExperiences(mathResponse.teachingExperiences())
        .university(mathResponse.university())
        .major(mathResponse.major())
        .highSchool(mathResponse.highSchool())
        .teachingStyle1(mathResponse.teachingStyle1())
        .teachingStyleInfo1(mathResponse.teachingStyleInfo1())
        .teachingStyle2(mathResponse.teachingStyle2())
        .teachingStyleInfo2(mathResponse.teachingStyleInfo2())
        .teachingStyle(mathCurriculumResponse.teachingStyle())
        .build();
  }

  private TeacherAllInformationResponse buildEnglishTeacherInfo(
      TeacherAllInformationResponse.TeacherAllInformationResponseBuilder builder,
      TeacherEnglishResponse englishResponse,
      EnglishCurriculumResponse englishCurriculumResponse) {
    return builder
        .subject(ClassType.영어)
        .comment(englishResponse.comment())
        .introduce(englishResponse.introduce())
        .teachingHistory(englishResponse.teachingHistory())
        .teachingExperiences(englishResponse.teachingExperiences())
        .foreignExperiences(englishResponse.foreignExperiences())
        .university(englishResponse.university())
        .major(englishResponse.major())
        .highSchool(englishResponse.highSchool())
        .teachingStyle1(englishResponse.teachingStyle1())
        .teachingStyleInfo1(englishResponse.teachingStyleInfo1())
        .teachingStyle2(englishResponse.teachingStyle2())
        .teachingStyleInfo2(englishResponse.teachingStyleInfo2())
        .teachingStyle(englishCurriculumResponse.teachingStyle())
        .video(englishCurriculumResponse.video())
        .build();
  }
}
