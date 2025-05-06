package com.yedu.api.domain.teacher.application.usecase;

import static com.yedu.api.domain.teacher.application.mapper.TeacherMapper.*;
import static com.yedu.api.global.event.mapper.BizppurioEventMapper.*;
import static com.yedu.api.global.event.mapper.DiscordEventMapper.*;

import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.api.domain.teacher.application.dto.req.AlarmTalkChangeRequest;
import com.yedu.api.domain.teacher.application.dto.req.AvailableChangeRequest;
import com.yedu.api.domain.teacher.application.dto.req.AvailableChangeTokenRequest;
import com.yedu.api.domain.teacher.application.dto.req.DistrictChangeRequest;
import com.yedu.api.domain.teacher.application.dto.req.TeacherContractRequest;
import com.yedu.api.domain.teacher.application.dto.req.TeacherInfoFormRequest;
import com.yedu.api.domain.teacher.application.dto.req.TeacherProfileFormRequest;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherAvailable;
import com.yedu.api.domain.teacher.domain.entity.TeacherDistrict;
import com.yedu.api.domain.teacher.domain.entity.TeacherEnglish;
import com.yedu.api.domain.teacher.domain.entity.TeacherMath;
import com.yedu.api.domain.teacher.domain.service.TeacherDeleteService;
import com.yedu.api.domain.teacher.domain.service.TeacherGetService;
import com.yedu.api.domain.teacher.domain.service.TeacherSaveService;
import com.yedu.api.domain.teacher.domain.service.TeacherUpdateService;
import com.yedu.api.global.config.s3.S3UploadService;
import com.yedu.cache.support.dto.TeacherNotifyApplicationFormDto;
import com.yedu.cache.support.storage.MatchingIdApplicationNotifyKeyStorage;
import com.yedu.cache.support.storage.TeacherNotifyApplicationFormKeyStorage;
import com.yedu.cache.support.storage.UpdateAvailableTimeKeyStorage;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TeacherManageUseCase {
  private final TeacherSaveService teacherSaveService;
  private final TeacherGetService teacherGetService;
  private final TeacherUpdateService teacherUpdateService;
  private final TeacherDeleteService teacherDeleteService;
  private final S3UploadService s3UploadService;
  private final ApplicationEventPublisher eventPublisher;
  private final TeacherNotifyApplicationFormKeyStorage teacherNotifyApplicationFormKeyStorage;
  private final ClassMatchingGetService classMatchingGetService;
  private final UpdateAvailableTimeKeyStorage updateAvailableTimeKeyStorage;
  private final MatchingIdApplicationNotifyKeyStorage matchingIdApplicationNotifyKeyStorage;

  public void saveTeacher(TeacherInfoFormRequest request) {
    Teacher teacher = mapToTeacher(request); // 기본 선생님 정보
    List<TeacherDistrict> teacherDistricts = getTeacherDistricts(request, teacher);
    List<TeacherAvailable> teacherAvailables = getTeacherAvailables(request.available(), teacher);
    TeacherEnglish english = getTeacherEnglish(request, teacher);
    TeacherMath math = getTeacherMath(request, teacher);
    teacherSaveService.saveTeacher(teacher, teacherAvailables, teacherDistricts, english, math);

    // 선생님 등록 1 알림톡 전송, 선생님 등록 2 알림톡 전송
    eventPublisher.publishEvent(mapToPhotoSubmitEvent(teacher));
  }

  private TeacherMath getTeacherMath(TeacherInfoFormRequest request, Teacher teacher) {
    TeacherMath math = null;
    if (request.mathPossible()) math = mapToTeacherMath(teacher, request);
    return math;
  }

  private TeacherEnglish getTeacherEnglish(TeacherInfoFormRequest request, Teacher teacher) {
    TeacherEnglish english = null;
    if (request.englishPossible()) english = mapToTeacherEnglish(teacher, request);
    return english;
  }

  private List<TeacherAvailable> getTeacherAvailables(
      List<List<String>> available, Teacher teacher) {
    List<TeacherAvailable> teacherAvailables = new ArrayList<>();

    for (int day = 0; day < available.size(); day++) {
      int finalDay = day;
      List<String> times = available.get(day);
      times.stream()
          .filter(time -> !time.equals("불가")) // "불가"가 아닌 값만 필터링
          .forEach(
              time -> {
                TeacherAvailable teacherAvailable = mapToTeacherAvailable(teacher, finalDay, time);
                teacherAvailables.add(teacherAvailable);
              });
    }
    // 선생님 가능 시간 : teacherAvailables 생성
    return teacherAvailables;
  }

  private List<TeacherDistrict> getTeacherDistricts(
      TeacherInfoFormRequest request, Teacher teacher) {
    return request.region().stream()
        .map(region -> mapToTeacherDistrict(teacher, region))
        .toList(); // 선생님 교육 가능 구역
  }

  public void saveTeacherProfile(MultipartFile profile, TeacherProfileFormRequest request) {
    String profileUrl = s3UploadService.saveProfileFile(profile);
    Teacher teacher = teacherGetService.byPhoneNumber(request.phoneNumber());
    teacherUpdateService.updateProfile(teacher, profileUrl);
    teacherUpdateService.updateFormStep(teacher);
    eventPublisher.publishEvent(mapToApplyAgreeEvent(teacher));
  }

  public void submitContract(TeacherContractRequest request) {
    Teacher teacher = teacherGetService.byPhoneNumber(request.phoneNumber());
    List<TeacherDistrict> teacherDistricts = teacherGetService.districtsByTeacher(teacher);
    teacherUpdateService.updateActive(teacher);
    eventPublisher.publishEvent(mapToInviteMatchingChannelInfoEvent(teacher));
    eventPublisher.publishEvent(mapToTeacherRegisterEvent(teacher, teacherDistricts));
  }

  public void notifyClass(List<ClassMatching> classMatchings, String applicationFormId) {
    classMatchings.forEach(
        classMatching -> {
          Teacher teacher = classMatching.getTeacher();
          teacherUpdateService.updateAlertCount(teacher);

          TeacherNotifyApplicationFormDto teacherNotifyApplicationFormDto =
              new TeacherNotifyApplicationFormDto(
                  classMatching.getClassMatchingId(), applicationFormId);
          String token =
              teacherNotifyApplicationFormKeyStorage.storeAndGet(teacherNotifyApplicationFormDto);
          matchingIdApplicationNotifyKeyStorage.store(classMatching.getClassMatchingId(), token);

          eventPublisher.publishEvent(mapToNotifyClassInfoEvent(classMatching, token));
        });
  }

  public void changeAlarmTalkStatus(AlarmTalkChangeRequest request) {
    Teacher teacher = teacherGetService.byNameAndPhoneNumber(request.name(), request.phoneNumber());
    teacherUpdateService.updateStatus(teacher, request.alarmTalk());
  }

  public void changeDistrict(DistrictChangeRequest request) {
    Teacher teacher = teacherGetService.byNameAndPhoneNumber(request.name(), request.phoneNumber());
    teacherDeleteService.districtByTeacher(teacher);
    List<TeacherDistrict> districts =
        request.districts().stream().map(region -> mapToTeacherDistrict(teacher, region)).toList();
    teacherSaveService.saveDistricts(districts);
  }

  public void changeAvailable(AvailableChangeRequest request) {
    Teacher teacher = teacherGetService.byNameAndPhoneNumber(request.name(), request.phoneNumber());
    teacherDeleteService.availableByTeacher(teacher);
    List<TeacherAvailable> availables = getTeacherAvailables(request.available(), teacher);
    teacherSaveService.saveAvailable(availables);
  }

  public void changeAvailableByToken(AvailableChangeTokenRequest request) {
    Long teacherId = updateAvailableTimeKeyStorage.get(request.token());
    if (teacherId != null) {
      Teacher teacher = teacherGetService.byId(teacherId);
      teacherDeleteService.availableByTeacher(teacher);
      List<TeacherAvailable> availables = mapToTeacherAvailable(teacher, request.dayTimes());
      teacherSaveService.saveAvailable(availables);
      return;
    }

    Long matchingId = teacherNotifyApplicationFormKeyStorage.get(request.token()).matchingId();
    Teacher teacher = classMatchingGetService.getById(matchingId).getTeacher();

    teacherDeleteService.availableByTeacher(teacher);
    List<TeacherAvailable> availables = mapToTeacherAvailable(teacher, request.dayTimes());
    teacherSaveService.saveAvailable(availables);
  }

  @Scheduled(cron = "0 0 20 * * *")
  public void remindAlarm() {
    log.info("리마인드 알림톡 전송 시작");
    teacherGetService
        .remindTeachers()
        .forEach(
            teacher -> {
              log.info("teacherId : " + teacher.getTeacherId() + " 리마인드 알림톡 전송");
              teacherUpdateService.updateRemind(teacher);
              eventPublisher.publishEvent(mapToPhotoHurryEvent(teacher));
            });
  }
}
