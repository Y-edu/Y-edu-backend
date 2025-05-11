package com.yedu.api.domain.matching.application.usecase;

import static com.yedu.api.global.event.mapper.BizppurioEventMapper.*;
import static com.yedu.api.global.event.mapper.DiscordEventMapper.*;

import com.yedu.api.domain.matching.application.dto.req.ChangeSessionDateRequest;
import com.yedu.api.domain.matching.application.dto.req.ClassScheduleConfirmRequest;
import com.yedu.api.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.api.domain.matching.application.dto.req.ClassScheduleRefuseRequest;
import com.yedu.api.domain.matching.application.dto.req.ClassScheduleRetrieveRequest;
import com.yedu.api.domain.matching.application.dto.req.CompleteSessionRequest;
import com.yedu.api.domain.matching.application.dto.req.CompleteSessionTokenRequest;
import com.yedu.api.domain.matching.application.dto.req.CreateScheduleRequest;
import com.yedu.api.domain.matching.application.dto.res.ClassScheduleRetrieveResponse;
import com.yedu.api.domain.matching.application.dto.res.RetrieveScheduleResponse;
import com.yedu.api.domain.matching.application.dto.res.SessionResponse;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSchedule;
import com.yedu.api.domain.matching.domain.entity.MatchingTimetable;
import com.yedu.api.domain.matching.domain.service.ClassManagementCommandService;
import com.yedu.api.domain.matching.domain.service.ClassManagementQueryService;
import com.yedu.api.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.api.domain.matching.domain.service.ClassSessionCommandService;
import com.yedu.api.domain.matching.domain.service.ClassSessionQueryService;
import com.yedu.api.domain.matching.domain.service.MatchingTimetableQueryService;
import com.yedu.api.domain.matching.domain.vo.ClassTime;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import com.yedu.cache.support.storage.KeyStorage;
import com.yedu.cache.support.storage.TokenStorage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Component
@Slf4j
public class ClassScheduleMatchingUseCase {

  private final ClassManagementCommandService managementCommandService;

  private final ClassManagementQueryService managementQueryService;

  private final KeyStorage<Long> classManagementKeyStorage;

  private final ApplicationEventPublisher eventPublisher;

  private final TokenStorage<Long> matchingIdApplicationNotifyKeyStorage;

  private final KeyStorage<Long> classMatchingKeyStorage;

  private final KeyStorage<Long> classSessionKeyStorage;

  private final MatchingTimetableQueryService matchingTimetableQueryService;
  private final ClassMatchingGetService classMatchingGetService;
  private final ClassManagementCommandService classManagementCommandService;
  private final ClassSessionCommandService classSessionCommandService;
  private final ClassSessionQueryService classSessionQueryService;
  private final ClassManagementQueryService classManagementQueryService;

  public String schedule(ClassScheduleMatchingRequest request) {
    String classNotifyToken = matchingIdApplicationNotifyKeyStorage.get(request.classMatchingId());
    ClassManagement classManagement = managementCommandService.schedule(request);
    String classManagementToken =
        classManagementKeyStorage.storeAndGet(classManagement.getClassManagementId());
    List<MatchingTimetable> timetables =
        matchingTimetableQueryService.query(
            classManagement.getClassMatching().getClassMatchingId());

    eventPublisher.publishEvent(mapToMatchingParentsEvent(classManagement));
    eventPublisher.publishEvent(
        mapToTeacherNotifyClassInfoEvent(
            classManagementToken, classNotifyToken, classManagement, timetables));
    eventPublisher.publishEvent(mapToTeacherScheduleEvent(classManagementToken, classManagement));

    return classManagementToken;
  }

  public void refuse(ClassScheduleRefuseRequest request) {
    classManagementKeyStorage.getAndExpire(
        request.classScheduleManagementId(),
        key -> {
          ClassMatching classMatching = managementCommandService.delete(request, key);
          eventPublisher.publishEvent(
              mapToScheduleCancelEvent(
                  classMatching.getTeacher(),
                  classMatching.getApplicationForm(),
                  request.refuseReason()));
        });
  }

  public void confirm(ClassScheduleConfirmRequest request) {
    classManagementKeyStorage.getAndExpire(
        request.classScheduleManagementId(),
        key -> {
          ClassManagement classManagement = managementCommandService.confirm(request, key);
          eventPublisher.publishEvent(mapToParentsClassInfoEvent(classManagement));
          eventPublisher.publishEvent(mapToMatchingConfirmTeacherEvent(classManagement));
        });
  }

  public ClassScheduleRetrieveResponse retrieve(ClassScheduleRetrieveRequest request) {
    Long id = classManagementKeyStorage.get(request.classScheduleManagementId());

    return managementQueryService
        .query(request, id)
        .map(ClassScheduleRetrieveResponse::of)
        .orElse(ClassScheduleRetrieveResponse.empty());
  }

  public SessionResponse create(CreateScheduleRequest request) {
    ClassMatching matching = getClassMatchingByToken(request.token());
    ClassManagement classManagement = classManagementCommandService.create(request, matching);

    classSessionCommandService.create(classManagement);

    return classSessionQueryService.query(matching);
  }

  public RetrieveScheduleResponse retrieveSchedule(String token) {
    ClassMatching matching = getClassMatchingByToken(token);
    ClassManagement classManagement =
        classManagementQueryService.queryWithSchedule(matching.getClassMatchingId()).orElse(null);

    if (classManagement == null || CollectionUtils.isEmpty(classManagement.getSchedules())) {
      return RetrieveScheduleResponse.empty();
    }

    Map<Day, List<ClassTime>> schedules =
        classManagement.getSchedules().stream()
            .collect(
                Collectors.groupingBy(
                    ClassSchedule::getDay,
                    Collectors.mapping(ClassSchedule::getClassTime, Collectors.toList())));

    return new RetrieveScheduleResponse(schedules);
  }

  public SessionResponse retrieveSession(String token) {
    ClassMatching matching = getClassMatchingByToken(token);

    ClassManagement classManagement =
        classManagementQueryService.queryWithSchedule(matching.getClassMatchingId()).orElse(null);

    if (classManagement == null) {
      return SessionResponse.empty();
    }

    classSessionCommandService.create(classManagement);
    return classSessionQueryService.query(matching);
  }

  public void changeSessionDate(Long sessionId, ChangeSessionDateRequest request) {
    classSessionCommandService.change(sessionId, request.sessionDate(), request.start());
  }

  public void cancelSession(Long sessionId, String cancelReason) {
    classSessionCommandService.cancel(sessionId, cancelReason);
  }

  public void revertCancelSession(Long sessionId) {
    classSessionCommandService.revertCancel(sessionId);
  }

  public void completeSession(Long sessionId, CompleteSessionRequest request) {
    classSessionCommandService.complete(sessionId, request);
  }

  public void completeSessionByToken(CompleteSessionTokenRequest request) {
    Long sessionId = classSessionKeyStorage.get(request.token());

    this.completeSession(
        sessionId,
        new CompleteSessionRequest(request.understanding(), request.homeworkPercentage()));
  }

  private ClassMatching getClassMatchingByToken(String token) {
    return Optional.ofNullable(classSessionKeyStorage.get(token))
        .map(classMatchingGetService::getBySessionId)
        .orElseGet(
            () -> {
              Long matchingId = classMatchingKeyStorage.get(token);
              if (matchingId == null) {
                throw new IllegalArgumentException("잘못된 토큰값입니다");
              }
              return classMatchingGetService.getById(matchingId);
            });
  }
}
