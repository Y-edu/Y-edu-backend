package com.yedu.api.domain.matching.domain.service;

import com.yedu.api.domain.matching.application.dto.res.RetrieveSessionDateResponse;
import com.yedu.api.domain.matching.application.dto.res.SessionResponse;
import com.yedu.api.domain.matching.application.dto.res.SessionResponse.Schedule;
import com.yedu.api.domain.matching.application.dto.res.SessionResponse.ScheduleInfo;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.entity.constant.CancelReason;
import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.api.domain.matching.domain.repository.ClassSessionRepository;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.SessionChangeForm;
import com.yedu.api.domain.parents.domain.repository.SessionChangeFormRepository;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClassSessionQueryService {

  private final ClassManagementQueryService classManagementQueryService;

  private final ClassSessionRepository classSessionRepository;
  private final SessionChangeFormRepository sessionChangeFormRepository;

  /**
   * 전화번호 기반으로 선생님의 수업 정보 조회
   */
  public SessionResponse query(
      List<ClassMatching> classMatchings,
      Boolean isComplete,
      Pageable pageable) {
    LocalDate now = LocalDate.now();
    LocalDate startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1L);
    LocalDate endOfMonth = now.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
    AtomicBoolean first = new AtomicBoolean(true);
      Map<String, ScheduleInfo> scheduleMap =
              IntStream.range(0, classMatchings.size())
                .mapToObj(i -> {
                  ClassMatching matching = classMatchings.get(i);
                  ApplicationForm applicationForm = matching.getApplicationForm();
                  Optional<ClassManagement> optionalManagement =
                      classManagementQueryService.query(matching.getClassMatchingId());

                  if (optionalManagement.isEmpty()) {
                    return null;
                  }

                  ClassManagement cm = optionalManagement.get();

                  Page<ClassSession> sessions =
                      Optional.ofNullable(isComplete)
                          .map(
                              complete ->
                                  classSessionRepository
                                      .findByClassManagementAndSessionDateBetweenAndCompleted(
                                          cm, startOfMonth, endOfMonth, isComplete, pageable))
                          .orElseGet(
                              () ->
                                  classSessionRepository.findByClassManagementAndSessionDateBetween(
                                      cm, startOfMonth, endOfMonth, pageable));

                  // Page<ClassSession> → Page<Schedule>
                  Page<Schedule> schedulePage =
                      SessionResponse.from(sessions, applicationForm.maxRoundNumber());
                  boolean isFirst = first.getAndSet(false);
                  return Map.entry(
                      applicationForm.getApplicationFormId(),
                      new ScheduleInfo(schedulePage, isFirst)); // send는 항상 첫번째
                })
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      return getSessionResponse(classMatchings, scheduleMap);
  }

  public SessionResponse query(
      ClassMatching tokenClassMatching,
      List<ClassMatching> classMatchings,
      Boolean isComplete,
      Pageable pageable) {
    LocalDate now = LocalDate.now();
    LocalDate startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1L);
    LocalDate endOfMonth = now.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

    Map<String, ScheduleInfo> scheduleMap =
        classMatchings.stream()
            .map(
                matching -> {
                  ApplicationForm applicationForm = matching.getApplicationForm();
                  Optional<ClassManagement> optionalManagement =
                      classManagementQueryService.query(matching.getClassMatchingId());

                  if (optionalManagement.isEmpty()) {
                    return null;
                  }

                  ClassManagement cm = optionalManagement.get();

                  Page<ClassSession> sessions =
                      Optional.ofNullable(isComplete)
                          .map(
                              complete ->
                                  classSessionRepository
                                      .findByClassManagementAndSessionDateBetweenAndCompleted(
                                          cm, startOfMonth, endOfMonth, isComplete, pageable))
                          .orElseGet(
                              () ->
                                  classSessionRepository.findByClassManagementAndSessionDateBetween(
                                      cm, startOfMonth, endOfMonth, pageable));

                  // Page<ClassSession> → Page<Schedule>
                  Page<Schedule> schedulePage =
                      SessionResponse.from(sessions, cm.maxRoundNumber());

                  return Map.entry(
                      applicationForm.getApplicationFormId(),
                      new ScheduleInfo(
                          schedulePage,
                          matching.getClassMatchingId()
                              == tokenClassMatching.getClassMatchingId()));
                })
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      return getSessionResponse(classMatchings, scheduleMap);
  }

    private SessionResponse getSessionResponse(List<ClassMatching> classMatchings, Map<String, ScheduleInfo> scheduleMap) {
        Map<String, MatchingStatus> matchingStatusesMap =
            classMatchings.stream()
                .map(
                    matching -> {
                      String applicationFormId = matching.getApplicationForm().getApplicationFormId();
                      return Map.entry(applicationFormId, matching.getMatchStatus());
                    })
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        return new SessionResponse(scheduleMap, matchingStatusesMap);
    }

    public RetrieveSessionDateResponse querySessionDate(Long sessionId) {
    return classSessionRepository
        .findById(sessionId)
        .map(
            it ->
                new RetrieveSessionDateResponse(
                    it.getSessionDate(),
                    it.isCompleted(),
                    it.getClassManagement().getClassMatching().getTeacher().getTeacherId(),
                    it.getClassTime()))
        .orElseThrow();
  }

  public List<ClassSession> query(ClassManagement management) {
    LocalDate now = LocalDate.now();
    return classSessionRepository.findByClassManagementAndSessionDateBetween(
        management,
        now.with(TemporalAdjusters.firstDayOfMonth()),
        now.with(TemporalAdjusters.lastDayOfMonth()));
  }

  public List<ClassSession> queryAll(ClassManagement management) {
    return classSessionRepository.findAllByClassManagement(management);
  }

  @Async
  public CompletableFuture<Integer> sumClassTimeAsync(
      ClassMatching matching, LocalDate startDate, LocalDate endDate) {
    Integer sum = classSessionRepository.sumClassTime(matching.getClassMatchingId(), startDate, endDate);
    return CompletableFuture.completedFuture(sum != null ? sum : 0);
  }

  public Map<ClassSession, Pair<ClassManagement, List<SessionChangeForm>>> query(List<ClassMatching> matchings) {
    List<ClassManagement> managements = classManagementQueryService.query(matchings);
    if (managements.isEmpty()) {
      return Collections.emptyMap();
    }

    LocalDate startDate = LocalDate.now();
    LocalDate endDate = startDate.plusMonths(3).with(TemporalAdjusters.lastDayOfMonth());

    List<ClassSession> sessions =
        classSessionRepository.findSession(managements, startDate, endDate);

    if (sessions.isEmpty()) {
      return Collections.emptyMap();
    }

    Map<ClassSession, ClassManagement> sessionToManagement = sessions.stream()
        .collect(Collectors.toMap(
            session -> session,
            ClassSession::getClassManagement
        ));

    List<SessionChangeForm> sessionChangeForms =
        sessionChangeFormRepository.findByLastSessionBeforeChangeIn(sessions);

    Map<ClassSession, List<SessionChangeForm>> formsMap =
        sessionChangeForms.stream()
            .collect(Collectors.groupingBy(SessionChangeForm::getLastSessionBeforeChange));

    Map<ClassSession, Pair<ClassManagement, List<SessionChangeForm>>> result = new LinkedHashMap<>();
    for (ClassSession session : sessions) {
      ClassManagement management = sessionToManagement.get(session);
      List<SessionChangeForm> forms = formsMap.getOrDefault(session, new ArrayList<>());
      result.put(session, Pair.of(management, forms));
    }

    return result;
  }
}
