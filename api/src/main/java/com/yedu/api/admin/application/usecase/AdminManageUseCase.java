package com.yedu.api.admin.application.usecase;

import static com.yedu.api.global.event.mapper.BizppurioEventMapper.*;

import com.yedu.api.admin.application.dto.req.ParentsKakaoNameRequest;
import com.yedu.api.admin.application.dto.req.ProposalTeacherRequest;
import com.yedu.api.admin.application.dto.req.RecommendTeacherRequest;
import com.yedu.api.admin.application.dto.req.TeacherIssueRequest;
import com.yedu.api.admin.application.dto.req.TeacherVideoRequest;
import com.yedu.api.admin.application.dto.res.ProposalTeacherResponse;
import com.yedu.api.admin.application.dto.res.ProposalTeacherResponse.TokenResponse;
import com.yedu.api.admin.domain.service.AdminGetService;
import com.yedu.api.admin.domain.service.AdminSaveService;
import com.yedu.api.admin.domain.service.AdminUpdateService;
import com.yedu.api.domain.matching.application.mapper.ClassMatchingMapper;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.service.TeacherUpdateService;
import com.yedu.cache.support.dto.MatchingTimeTableDto;
import com.yedu.cache.support.dto.TeacherNotifyApplicationFormDto;
import com.yedu.cache.support.storage.KeyStorage;
import com.yedu.cache.support.storage.MatchingIdApplicationNotifyKeyStorage;
import com.yedu.cache.support.storage.ResponseRateStorage;
import com.yedu.cache.support.storage.TeacherNotifyApplicationFormKeyStorage;
import com.yedu.common.event.bizppurio.RecommendTeacherEvent;
import com.yedu.common.type.ClassType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminManageUseCase {
  private final AdminGetService adminGetService;
  private final AdminUpdateService adminUpdateService;
  private final AdminSaveService adminSaveService;
  private final ResponseRateStorage responseRateStorage;
  private final TeacherUpdateService teacherUpdateService;
  private final ApplicationEventPublisher eventPublisher;
  private final KeyStorage<MatchingTimeTableDto> matchingTimetableKeyStorage;
  private final TeacherNotifyApplicationFormKeyStorage teacherNotifyApplicationFormKeyStorage;
  private final MatchingIdApplicationNotifyKeyStorage matchingIdApplicationNotifyKeyStorage;
  private final ClassMatchingGetService classMatchingGetService;

  public void updateParentsKakaoName(long parentsId, ParentsKakaoNameRequest request) {
    Parents parents = adminGetService.parentsById(parentsId);
    adminUpdateService.updateKakaoName(parents, request.kakaoName());
  }

  public void updateProceedStatus(String applicationFormId) {
    ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
    adminUpdateService.updateProceedStatus(applicationForm);
  }

  public void updateTeacherIssue(long teacherId, TeacherIssueRequest request) {
    Teacher teacher = adminGetService.teacherById(teacherId);
    adminUpdateService.updateTeacherIssue(teacher, request.issue());
  }

  public void updateTeacherVideo(long teacherId, TeacherVideoRequest request) {
    Teacher teacher = adminGetService.teacherById(teacherId);
    adminUpdateService.updateTeacherVideo(teacher, request.video());
  }

  public void recommendTeacher(RecommendTeacherRequest request) {
    List<RecommendTeacherEvent> recommendTeacherEvents =
        request.classMatchingIds().stream()
            .map(
                id -> {
                  ClassMatching classMatching = adminGetService.classMatchingById(id);
                  adminUpdateService.updateClassMatchingSend(classMatching);
                  String token = createMatchingTimeTableToken(classMatching);
                  return mapToRecommendTeacherEvent(classMatching, token);
                })
            .toList();
    recommendTeacherEvents.forEach(eventPublisher::publishEvent);
    RecommendTeacherEvent recommendTeacherEvent = recommendTeacherEvents.get(0);
    eventPublisher.publishEvent(
        mapToRecommendGuideEvent(recommendTeacherEvent.parentsPhoneNumber()));
  }

  public Pair<String, ClassType> retrieveRecommendTeacherToken(Long classMatchingId) {
    ClassMatching matching = classMatchingGetService.getById(classMatchingId);
    long teacherId = matching.getTeacher().getTeacherId();
    ClassType wantedSubject = matching.getApplicationForm().getWantedSubject();

    MatchingTimeTableDto key = new MatchingTimeTableDto(teacherId, classMatchingId, wantedSubject);

    return Pair.of(matchingTimetableKeyStorage.storeAndGet(key), wantedSubject);
  }

  private String createMatchingTimeTableToken(ClassMatching classMatching) {
    Teacher teacher = classMatching.getTeacher();
    long teacherId = teacher.getTeacherId();
    ApplicationForm applicationForm = classMatching.getApplicationForm();
    ClassType wantedSubject = applicationForm.getWantedSubject();
    return matchingTimetableKeyStorage.storeAndGet(
        new MatchingTimeTableDto(teacherId, classMatching.getClassMatchingId(), wantedSubject));
  }

  public ProposalTeacherResponse proposalTeacher(
      String applicationFormId, ProposalTeacherRequest request) {
    ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);

    List<TokenResponse> responses =
        request.teacherIds().stream()
            .map(teacherId -> handleOneTeacher(teacherId, applicationFormId, applicationForm))
            .toList();

    return new ProposalTeacherResponse(responses);
  }

  private TokenResponse handleOneTeacher(
      Long teacherId, String applicationFormId, ApplicationForm applicationForm) {
    Teacher teacher = adminGetService.teacherById(teacherId);
    teacherUpdateService.plusRequestCount(teacher);
    responseRateStorage.cache(teacher.getTeacherId());

    ClassMatching classMatching = ClassMatchingMapper.mapToClassMatching(teacher, applicationForm);
    adminSaveService.saveClassMatching(classMatching);
    adminUpdateService.updateAlertCount(teacher);

    String token =
        teacherNotifyApplicationFormKeyStorage.storeAndGet(
            new TeacherNotifyApplicationFormDto(
                classMatching.getClassMatchingId(), applicationFormId));
    matchingIdApplicationNotifyKeyStorage.store(classMatching.getClassMatchingId(), token);

    eventPublisher.publishEvent(mapToNotifyClassInfoEvent(classMatching, token));

    return new TokenResponse(
        teacher.getTeacherId(),
        applicationFormId,
        teacher.getTeacherInfo().getPhoneNumber(),
        token);
  }
}
