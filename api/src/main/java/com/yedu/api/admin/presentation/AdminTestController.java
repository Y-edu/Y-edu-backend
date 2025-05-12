package com.yedu.api.admin.presentation;

import static com.yedu.api.global.event.mapper.BizppurioEventMapper.*;
import static com.yedu.api.global.event.mapper.BizppurioEventMapper.mapToPhotoSubmitEvent;

import com.yedu.api.admin.domain.service.AdminGetService;
import com.yedu.api.domain.matching.application.mapper.ClassMatchingMapper;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.api.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.parents.domain.repository.ApplicationFormRepository;
import com.yedu.api.domain.parents.domain.repository.GoalRepository;
import com.yedu.api.domain.parents.domain.repository.ParentsRepository;
import com.yedu.api.domain.parents.domain.service.ParentsSaveService;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherAvailable;
import com.yedu.api.domain.teacher.domain.entity.TeacherClassInfo;
import com.yedu.api.domain.teacher.domain.entity.TeacherDistrict;
import com.yedu.api.domain.teacher.domain.entity.TeacherEnglish;
import com.yedu.api.domain.teacher.domain.entity.TeacherInfo;
import com.yedu.api.domain.teacher.domain.entity.TeacherMath;
import com.yedu.api.domain.teacher.domain.entity.TeacherSchoolInfo;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import com.yedu.api.domain.teacher.domain.entity.constant.District;
import com.yedu.api.domain.teacher.domain.entity.constant.TeacherGender;
import com.yedu.api.domain.teacher.domain.entity.constant.TeacherStatus;
import com.yedu.api.domain.teacher.domain.entity.constant.TeachingStyle;
import com.yedu.api.domain.teacher.domain.repository.TeacherAvailableRepository;
import com.yedu.api.domain.teacher.domain.repository.TeacherDistrictRepository;
import com.yedu.api.domain.teacher.domain.repository.TeacherEnglishRepository;
import com.yedu.api.domain.teacher.domain.repository.TeacherMathRepository;
import com.yedu.api.domain.teacher.domain.repository.TeacherRepository;
import com.yedu.api.domain.teacher.domain.service.TeacherGetService;
import com.yedu.api.domain.teacher.domain.service.TeacherSaveService;
import com.yedu.cache.support.dto.TeacherNotifyApplicationFormDto;
import com.yedu.cache.support.storage.ClassMatchingKeyStorage;
import com.yedu.cache.support.storage.ClassSessionKeyStorage;
import com.yedu.cache.support.storage.MatchingIdApplicationNotifyKeyStorage;
import com.yedu.cache.support.storage.TeacherNotifyApplicationFormKeyStorage;
import com.yedu.cache.support.storage.UpdateAvailableTimeKeyStorage;
import com.yedu.common.event.bizppurio.TeacherAvailableTimeUpdateRequestEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "ADMIN TEST Controller", description = "여러가지 테스트용 API입니다")
@Transactional
public class AdminTestController {
  private final TeacherSaveService teacherSaveService;
  private final TeacherGetService teacherGetService;
  private final AdminGetService adminGetService;
  private final ParentsSaveService parentsSaveService;
  private final ApplicationFormRepository applicationFormRepository;
  private final ParentsRepository parentsRepository;
  private final GoalRepository goalRepository;
  private final ClassMatchingRepository classMatchingRepository;
  private final TeacherAvailableRepository teacherAvailableRepository;
  private final TeacherDistrictRepository teacherDistrictRepository;
  private final TeacherEnglishRepository teacherEnglishRepository;
  private final TeacherMathRepository teacherMathRepository;
  private final TeacherRepository teacherRepository;
  private final ClassMatchingGetService classMatchingGetService;
  private final ApplicationEventPublisher eventPublisher;
  private final TeacherNotifyApplicationFormKeyStorage teacherNotifyApplicationFormKeyStorage;
  private final MatchingIdApplicationNotifyKeyStorage matchingIdApplicationNotifyKeyStorage;
  private final UpdateAvailableTimeKeyStorage updateAvailableTimeKeyStorage;
  private final ClassMatchingKeyStorage classMatchingKeyStorage;
  private final ClassSessionKeyStorage classSessionKeyStorage;

  @PostMapping("/test/teacher/signup/{phoneNumber}")
  @Operation(summary = "선생님 간편 가입 - 전화번호를 넣어주세요 (간편가입이라 내용은 기대하지 마세요)")
  public void signUpTeacher(@PathVariable String phoneNumber) {
    Teacher teacher =
        Teacher.builder()
            .teacherInfo(
                new TeacherInfo(
                    "채영대", "토마스", "a@a.com", phoneNumber, "1998", TeacherGender.남, "", ""))
            .teacherClassInfo(
                new TeacherClassInfo(
                    "나는 이러한 선생님",
                    TeachingStyle.CARING,
                    TeachingStyle.FUN,
                    "저런 방식",
                    "저런 방식",
                    "# 이런 학생",
                    "이러이러하다",
                    true,
                    true))
            .teacherSchoolInfo(new TeacherSchoolInfo("대학교", true, "학과", "고등학교", "외고"))
            .source("기타")
            .status(TeacherStatus.활동중)
            .build();
    TeacherEnglish teacherEnglish =
        TeacherEnglish.builder()
            .teacher(teacher)
            .foreignExperience("# abc")
            .teachingExperience("# abc")
            .teachingHistory(3)
            .teachingStyle("이러이러")
            .build();
    TeacherMath teacherMath =
        TeacherMath.builder()
            .teacher(teacher)
            .teachingExperience("# abc")
            .teachingHistory(3)
            .teachingStyle("이러이러")
            .build();
    teacherSaveService.saveTeacher(
        teacher,
        List.of(
            TeacherAvailable.builder()
                .teacher(teacher)
                .day(Day.byInt(2))
                .availableTime(LocalTime.parse("12:00"))
                .build()),
        List.of(TeacherDistrict.builder().teacher(teacher).district(District.강남구).build()),
        teacherEnglish,
        teacherMath);
  }

  @PostMapping("/test/parents/signup/{phoneNumber}")
  @Operation(summary = "학부모 간편 가입 - 전화번호를 넣어주세요 (간편가입이라 내용은 기대하지 마세요)")
  public void signUpParents(@PathVariable String phoneNumber) {
    Parents parents = Parents.builder().phoneNumber(phoneNumber).marketingAgree(true).build();
    parentsSaveService.saveParents(parents);
  }

  @DeleteMapping("/test/teacher/{phoneNumber}")
  @Operation(summary = "선생님 간편 삭제 - 전화번호를 넣어주세요")
  public void deleteTeacher(@PathVariable String phoneNumber) {
    classMatchingRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
    teacherAvailableRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
    teacherDistrictRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
    teacherEnglishRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
    teacherEnglishRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
    teacherMathRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
    teacherRepository.deleteAllByTeacherInfo_PhoneNumber(phoneNumber);
  }

  @DeleteMapping("/test/parents/{phoneNumber}")
  @Operation(summary = "학부모 간편 삭제 - 전화번호를 넣어주세요")
  public void deleteParents(@PathVariable String phoneNumber) {
    classMatchingRepository.deleteAllByApplicationForm_Parents_PhoneNumber(phoneNumber);
    goalRepository.deleteAllByApplicationForm_Parents_PhoneNumber(phoneNumber);
    applicationFormRepository.deleteAllByParents_PhoneNumber(phoneNumber);
    parentsRepository.deleteAllByPhoneNumber(phoneNumber);
  }

  @PostMapping("/test/teacher/profile/{phoneNumber}")
  @Operation(summary = "구글폼 프로필 작성시 받는 알림톡 - 받을 사람의 전화번호를 적어주세요! 단, 이미 가입은 이미 선생님으로 했어야 합니다!")
  public void finProfile(@PathVariable String phoneNumber) {
    Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);
    eventPublisher.publishEvent(mapToPhotoSubmitEvent(teacher));
  }

  @PostMapping("/test/teacher/photo/{phoneNumber}")
  @Operation(summary = "구글폼 사진 및 영상 제출시 받는 알림톡 - 받을 사람의 전화번호를 적어주세요! 단, 이미 이미 선생님으로 가입은 했어야 합니다!")
  public void finPhoto(@PathVariable String phoneNumber) {
    Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);
    eventPublisher.publishEvent(mapToApplyAgreeEvent(teacher));
  }

  @PostMapping("/test/teacher/agree/{phoneNumber}")
  @Operation(summary = "계약서 제출시 받는 알림톡 - 받을 사람의 전화번호를 적어주세요! 단, 이미 이미 선생님으로 가입은 했어야 합니다!")
  public void finApplyAgree(@PathVariable String phoneNumber) {
    Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);
    eventPublisher.publishEvent(mapToInviteMatchingChannelInfoEvent(teacher));
  }

  @PostMapping("/test/teacher/recommend/{applicationFormId}/{phoneNumber}")
  @Operation(
      summary =
          "선생님에게 수업 추천해주는 알림톡 - 관리자 페이지 확인 후 신청건ID(EX. 온라인11a)와 받을 사람의 전화번호를 적어주세요! 단, 이미 이미 선생님으로 가입은 했어야 합니다!")
  public void recommendClass(
      @PathVariable String applicationFormId, @PathVariable String phoneNumber) {
    ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
    Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);

    Optional<ClassMatching> existingClassMatching =
        classMatchingRepository
            .findByApplicationForm_ApplicationFormIdAndTeacher_TeacherIdAndTeacher_TeacherInfo_PhoneNumber(
                applicationForm.getApplicationFormId(),
                teacher.getTeacherId(),
                teacher.getTeacherInfo().getPhoneNumber());
    existingClassMatching.ifPresentOrElse(
        classMatching -> {}, // 기존 매칭이 존재하면 아무 작업 안 함
        () -> {
          ClassMatching newClassMatching =
              ClassMatchingMapper.mapToClassMatching(teacher, applicationForm);
          ClassMatching classMatching = classMatchingRepository.save(newClassMatching);
          TeacherNotifyApplicationFormDto teacherNotifyApplicationFormDto =
              new TeacherNotifyApplicationFormDto(
                  classMatching.getClassMatchingId(), applicationFormId);

          String token =
              teacherNotifyApplicationFormKeyStorage.storeAndGet(teacherNotifyApplicationFormDto);
          matchingIdApplicationNotifyKeyStorage.store(classMatching.getClassMatchingId(), token);

          eventPublisher.publishEvent(mapToNotifyClassInfoEvent(classMatching, token));
        });
  }

  // 실제로 매칭을 하나 생성해줘야함

  @PostMapping("/test/teacher/accept/{applicationFormId}/{phoneNumber}")
  @Operation(
      summary =
          "선생님이 수업 신청시 받는 알림톡 - 관리자 페이지 확인 후 신청건ID(EX. 온라인11a)와 받을 사람의 전화번호를 적어주세요! 단, 이미 선생님으로 이미 가입은 했어야 합니다!")
  public void acceptCase(@PathVariable String phoneNumber, @PathVariable String applicationFormId) {
    Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);
    ClassMatching classMatching =
        classMatchingGetService.classMatchingByApplicationFormIdAndTeacherId(
            applicationFormId, teacher.getTeacherId(), phoneNumber);
    eventPublisher.publishEvent(mapToMatchingAcceptCaseEvent(classMatching));
  }

  @PostMapping("/test/teacher/refuse/{phoneNumber}")
  @Operation(
      summary =
          "선생님이 수업 거절시 받는 알림톡 - 관리자 페이지 확인 후 신청건ID(EX. 온라인11a)와 받을 사람의 전화번호를 적어주세요! 단, 이미 선생님으로 가입은 했어야 합니다!")
  public void refuseCase(@PathVariable String phoneNumber) {
    Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);
    eventPublisher.publishEvent(mapToMatchingRefuseCaseEvent(teacher));
  }

  @PostMapping("/test/teacher/{phoneNumber}")
  @Operation(summary = "선생님의 전화번호로 가능시간 갱신 요청 알림톡 발송")
  public void sendAvailableAlarm(@PathVariable String phoneNumber) {
    Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);
    TeacherInfo teacherInfo = teacher.getTeacherInfo();
    String token = updateAvailableTimeKeyStorage.storeAndGet(teacher.getTeacherId());
    TeacherAvailableTimeUpdateRequestEvent event =
        new TeacherAvailableTimeUpdateRequestEvent(teacherInfo.getNickName(), token, phoneNumber);
    eventPublisher.publishEvent(event);
  }

  @PostMapping("/test/schedule")
  @Operation(
      summary = "시간/날짜 등록 안된 선생님의 과외 완료,날짜 변경/휴강 토큰 발급",
      tags = {"완료톡 관련 API"})
  public String issueChangeSessionDateToken(
      @RequestParam String applicationFormId, @RequestParam String teacherPhoneNumber) {
    ApplicationForm applicationForm =
        applicationFormRepository
            .findById(applicationFormId)
            .orElseThrow(
                () -> {
                  throw new IllegalArgumentException("존재하지 않는 과외 식별자입니다.");
                });

    List<ClassMatching> matchings = classMatchingGetService.getByApplicationForm(applicationForm);

    for (ClassMatching matching : matchings) {
      if (matching.getTeacher().getTeacherInfo().getPhoneNumber().equals(teacherPhoneNumber)) {
        return classMatchingKeyStorage.storeAndGet(matching.getClassMatchingId());
      }
    }
    throw new IllegalArgumentException("매칭에 존재하지 않는 선생님 번호입니다.");
  }

  @PostMapping("/test/schedule/complete")
  @Operation(
      summary = "시간/날짜 등록된 선생님의 과외 완료 토큰 발급",
      tags = {"완료톡 관련 API"})
  public String issueCompleteSessionToken(@RequestParam Long classSessionId) {
    return classSessionKeyStorage.storeAndGet(classSessionId);
  }
}
