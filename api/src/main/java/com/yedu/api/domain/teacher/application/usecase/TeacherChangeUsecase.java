package com.yedu.api.domain.teacher.application.usecase;

import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormChangeRequest;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormRequest;
import com.yedu.api.domain.parents.application.usecase.ParentsManageUseCase;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Goal;
import com.yedu.api.domain.parents.domain.entity.SessionChangeForm;
import com.yedu.api.domain.parents.domain.entity.constant.SessionChangeType;
import com.yedu.api.domain.parents.domain.repository.SessionChangeFormRepository;
import com.yedu.api.domain.parents.domain.service.ParentsGetService;
import com.yedu.api.domain.teacher.domain.entity.constant.District;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TeacherChangeUsecase {

  private final SessionChangeFormRepository sessionChangeFormRepository;

  private final ClassMatchingRepository classMatchingRepository;
  private final ParentsManageUseCase parentsManageUseCase;
  private final ParentsGetService parentsGetService;

  public void change(ApplicationFormChangeRequest request) {
    String phoneNumber = request.phoneNumber();
    if (!phoneNumber.startsWith("0")) {
      phoneNumber = "0" + phoneNumber;
    }

    log.info(">>> 선생님 교체 신청 : {}", request);
    SessionChangeForm changeForm = sessionChangeFormRepository
        .findFirstByParents_PhoneNumberAndChangeTypeOrderByCreatedAtDesc(
            phoneNumber,
            SessionChangeType.CHANGE_TEACHER
        )
        .orElseThrow(() -> new IllegalArgumentException("제출된 선생님 교체 신청건이 존재하지 않습니다"));

    ClassSession lastSession = changeForm.getLastSessionBeforeChange();
    long matchingId = lastSession
        .getClassManagement()
        .getClassMatching()
        .getClassMatchingId();

    ClassMatching matching = classMatchingRepository.findById(matchingId).orElseThrow();
    ApplicationForm previousApplicationForm = matching.getApplicationForm();
    District previousDistrict = previousApplicationForm.getDistrict();
    long previousTeacherId = matching.getTeacher().getTeacherId();

    District newDistrict = District.fromString(request.district());
    if (previousDistrict.equals(newDistrict)) {
      log.info(">>> 지역 동일하여 기존 과외 재공지 :{}", newDistrict);

      parentsManageUseCase.sendAlarmTalk(previousApplicationForm, new ArrayList<>(),
          previousTeacherId);
      return;
    }

    log.info(">>> 지역 변경으로 인한 신규 과외 생성 :{}", newDistrict);
    List<String> classGoals = parentsGetService.goalsByApplicationForm(previousApplicationForm)
        .stream()
        .map(Goal::getClassGoal)
        .toList();

    parentsManageUseCase.saveParentsAndApplication(
        ApplicationFormRequest.builder()
            .phoneNumber(phoneNumber)
            .age(previousApplicationForm.getAge())
            .wantedSubject(previousApplicationForm.getWantedSubject())
            .wantedTime(request.wantedTime())
            .classGoals(classGoals)
            .favoriteGender(request.favoriteGender())
            .favoriteStyle(request.favoriteStyle())
            .online(previousApplicationForm.getOnline())
            .classCount(request.useSameClassCount() ? previousApplicationForm.getClassCount()
                : request.classCount())
            .classTime(request.useSameClassCount() ? previousApplicationForm.getClassTime()
                : request.classTime())
            .district(request.district())
            .dong(request.dong())
            .source(previousApplicationForm.getSource())
            .dayTimes(Collections.emptyList())
            .build(),
        false, previousTeacherId);
  }
}
