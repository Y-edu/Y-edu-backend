package com.yedu.api.domain.parents.application.usecase;

import static com.yedu.api.domain.parents.application.mapper.ParentsMapper.*;

import com.yedu.api.domain.matching.application.usecase.ClassMatchingManageUseCase;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.api.domain.matching.domain.service.ClassSessionQueryService;
import com.yedu.api.domain.parents.application.dto.req.AcceptChangeSessionRequest;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormChangeRequest;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormRequest;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormTimeTableRequest;
import com.yedu.api.domain.parents.application.dto.res.ApplicationFormTimeTableResponse;
import com.yedu.api.domain.parents.application.dto.res.ParentSessionResponse;
import com.yedu.api.domain.parents.application.mapper.ApplicationFormAvailableMapper;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.ApplicationFormAvailable;
import com.yedu.api.domain.parents.domain.entity.Goal;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.parents.domain.entity.SessionChangeForm;
import com.yedu.api.domain.parents.domain.entity.constant.SessionChangeType;
import com.yedu.api.domain.parents.domain.repository.ApplicationFormRepository;
import com.yedu.api.domain.parents.domain.repository.SessionChangeFormRepository;
import com.yedu.api.domain.parents.domain.service.ApplicationFormAvailableCommandService;
import com.yedu.api.domain.parents.domain.service.ApplicationFormAvailableQueryService;
import com.yedu.api.domain.parents.domain.service.ParentsGetService;
import com.yedu.api.domain.parents.domain.service.ParentsSaveService;
import com.yedu.api.domain.parents.domain.service.ParentsUpdateService;
import com.yedu.api.domain.parents.domain.service.SessionChangeFormCommandService;
import com.yedu.api.domain.teacher.application.usecase.TeacherInfoUseCase;
import com.yedu.api.domain.teacher.application.usecase.TeacherManageUseCase;
import com.yedu.api.domain.teacher.domain.aggregate.TeacherWithAvailable;
import com.yedu.api.domain.teacher.domain.entity.constant.District;
import com.yedu.cache.support.RedisRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ParentsManageUseCase {
  private final ParentsGetService parentsGetService;
  private final ParentsSaveService parentsSaveService;
  private final ParentsUpdateService parentsUpdateService;
  private final TeacherManageUseCase teacherManageUseCase;
  private final TeacherInfoUseCase teacherInfoUseCase;
  private final ClassMatchingManageUseCase classMatchingManageUseCase;
  private final ApplicationFormAvailableQueryService applicationFormAvailableQueryService;
  private final ApplicationFormAvailableCommandService applicationFormAvailableCommandService;
  private final RedisRepository redisRepository;
  private final ApplicationFormRepository applicationFormRepository;
  private final ClassMatchingGetService classMatchingGetService;
  private final ClassSessionQueryService classSessionQueryService;
  private final SessionChangeFormCommandService sessionChangeFormCommandService;
  private final SessionChangeFormRepository sessionChangeFormRepository;

  public void saveParentsAndApplication(ApplicationFormRequest request, boolean isResend) {

    Parents parents =
        parentsGetService
            .optionalParentsByPhoneNumber(request.phoneNumber())
            .orElseGet(() -> parentsSaveService.saveParents(mapToParents(request)));

    parentsUpdateService.updateCount(parents); // todo : 나중에 학부모 생성과 신청서 생성이 분리되면 나누어질 부분
    ApplicationForm applicationForm = mapToApplicationForm(parents, request);

    if (applicationFormRepository.findById(applicationForm.getApplicationFormId()).isPresent()) {
      log.warn("중복 제출- 과외 식별자 {}, {}", applicationForm.getApplicationFormId(), applicationForm);
      return;
    }

    if (applicationFormRepository
        .findByParentsAndDistrictAndWantedSubjectAndAgeAndClassCountAndCreatedAtAfter(
            applicationForm.getParents(),
            applicationForm.getDistrict(),
            applicationForm.getWantedSubject(),
            applicationForm.getAge(),
            applicationForm.getClassCount(),
            LocalDateTime.now().minusMinutes(1))
        .isPresent()) {
      log.warn("중복 제출- 과외 식별자 {}, {}", applicationForm.getApplicationFormId(), applicationForm);
      return;
    }

    //  applicationFormId를 제외한 모든 필드가 동일한 중복 신청서 체크 (최초발송)
    if (!isResend
        && applicationFormRepository
            .findByAllFieldsExceptId(
                applicationForm.getParents(),
                applicationForm.getAge(),
                applicationForm.getOnline(),
                applicationForm.getDistrict(),
                applicationForm.getDong(),
                applicationForm.getWantedSubject(),
                applicationForm.getLevel(),
                applicationForm.getFavoriteCondition(),
                applicationForm.getEducationImportance(),
                applicationForm.getFavoriteStyle(),
                applicationForm.getFavoriteGender(),
                applicationForm.getFavoriteDirection(),
                applicationForm.getWantTime(),
                applicationForm.getClassCount(),
                applicationForm.getClassTime(),
                applicationForm.getSource(),
                applicationForm.isProceedStatus(),
                applicationForm.getPay())
            .isPresent()) {
      log.warn(
          "중복 제출- 모든 필드 동일한 신청서 발견 {}, {}",
          applicationForm.getApplicationFormId(),
          applicationForm);
      return;
    }

    List<Goal> goals =
        request.classGoals().stream().map(goal -> mapToGoal(applicationForm, goal)).toList();
    parentsSaveService.saveApplication(applicationForm, goals);

    List<ApplicationFormAvailable> applicationFormAvailables =
        ApplicationFormAvailableMapper.map(
            request.dayTimes(), applicationForm.getApplicationFormId());
    applicationFormAvailableCommandService.saveAll(applicationFormAvailables);

    TeacherWithAvailable teacherWithAvailable =
        teacherInfoUseCase.allApplicationFormTeacher(applicationForm);
    int classCount = getClassCount(applicationForm.getClassCount());
    List<ClassMatching> classMatchings =
        classMatchingManageUseCase.saveAllClassMatching(
            teacherWithAvailable.availableTeacher(classCount, applicationFormAvailables),
            applicationForm); // 매칭 저장
    teacherManageUseCase.notifyClass(
        classMatchings, applicationForm.getApplicationFormId()); // 선생님한테 알림톡 전송
  }

  public ApplicationFormTimeTableResponse retrieveTimeTable(
      ApplicationFormTimeTableRequest request) {
    if (StringUtils.hasText(request.applicationFormId())) {
      return Optional.of(applicationFormAvailableQueryService.query(request.applicationFormId()))
          .map(ApplicationFormAvailableMapper::map)
          .get();
    }

    return redisRepository
        .getValues(request.token()) // todo 토큰저장 & 조회로직 분리
        .map(applicationFormAvailableQueryService::query)
        .map(ApplicationFormAvailableMapper::map)
        .orElseGet(ApplicationFormTimeTableResponse::empty);
  }

  public void resendParentsAndApplication(ApplicationFormRequest request) {
    saveParentsAndApplication(request, true);
  }

  public List<ParentSessionResponse> retrieveSessions(String phoneNumber) {
    Parents parent =
        parentsGetService
            .optionalParentsByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학부모 핸드폰번호입니다."));

    List<ClassMatching> matchings = classMatchingGetService.getMatched(parent);
    Map<ClassSession, List<SessionChangeForm>> sessions = classSessionQueryService.query(matchings);

    return ParentSessionResponse.of(sessions);
  }

  public void acceptChangeSessionForm(String phoneNumber, AcceptChangeSessionRequest request) {
    Parents parent =
        parentsGetService
            .optionalParentsByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학부모 핸드폰번호입니다."));

    sessionChangeFormCommandService.save(parent, request);
  }

  public void changeApplication(ApplicationFormChangeRequest request) {
    log.info(">>> 선생님 교체 신청 :{}", request);

    SessionChangeForm changeForm =
        sessionChangeFormRepository
            .findFirstByParents_PhoneNumberAndChangeType(
                request.phoneNumber(), SessionChangeType.CHANGE_TEACHER)
            .orElseThrow(() -> new IllegalArgumentException("제출된 선생님 교체 신청건이 존재하지 않습니다"));

    ApplicationForm previousApplicationForm =
        changeForm
            .getLastSessionBeforeChange()
            .getClassManagement()
            .getClassMatching()
            .getApplicationForm();

    District previousDistrict = previousApplicationForm.getDistrict();

    if (previousDistrict.equals(request.district())) {
      // TODO 선생님 교체만 진행하는 경우 개발
      return;
    }
    // 신규 과외 생성
    List<String> classGoals =
        parentsGetService.goalsByApplicationForm(previousApplicationForm).stream()
            .map(Goal::getClassGoal)
            .toList();

    saveParentsAndApplication(
        ApplicationFormRequest.builder()
            .phoneNumber(request.phoneNumber())
            .age(previousApplicationForm.getAge())
            .wantedSubject(previousApplicationForm.getWantedSubject())
            .wantedTime(request.wantedTime())
            .classGoals(classGoals)
            .favoriteGender(request.favoriteGender())
            .favoriteStyle(request.favoriteStyle())
            .online(previousApplicationForm.getOnline())
            .classCount(request.classCount())
            .classTime(request.classTime())
            .district(request.district())
            .dong(request.dong())
            .source(previousApplicationForm.getSource())
            .dayTimes(Collections.emptyList())
            .build(),
        false);
  }
}
