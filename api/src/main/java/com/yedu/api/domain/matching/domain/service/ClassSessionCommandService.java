package com.yedu.api.domain.matching.domain.service;

import com.yedu.api.domain.matching.application.dto.req.CompleteSessionRequest;
import com.yedu.api.domain.matching.domain.entity.constant.CancelReason;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSchedule;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.repository.ClassManagementRepository;
import com.yedu.api.domain.matching.domain.repository.ClassSessionRepository;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClassSessionCommandService {

  private final ClassSessionRepository classSessionRepository;
  private final ClassMatchingGetService classMatchingGetService;
  private final ClassManagementRepository classManagementRepository;

  private Map<LocalDate, ClassSession> mapSessionsByDate(List<ClassSession> sessions) {
    return sessions.stream()
        .collect(
            Collectors.toMap(
                ClassSession::getSessionDate,
                session -> session,
                (existing, duplicate) -> existing));
  }

  private List<ClassSession> generateNewSessions(
      List<ClassSchedule> schedules,
      ClassManagement classManagement,
      Map<LocalDate, ClassSession> existingSessionMap,
      LocalDate today,
      LocalDate changeStartDate) {

    return schedules.stream()
        .flatMap(
            schedule ->
                schedule
                    .generateUpcomingDates(
                        classManagement, today, existingSessionMap, changeStartDate)
                    .stream())
        .toList();
  }

  public ClassSession cancel(Long sessionId, CancelReason cancelReason, Boolean isTodayCancel) {
    ClassSession session = findSessionById(sessionId);

    session.cancel(cancelReason.name(), isTodayCancel);

    return session;
  }

  public ClassSession revertCancel(Long sessionId) {
    ClassSession session = findSessionById(sessionId);

    session.revertCancel();

    return session;
  }

  public ClassSession complete(Long sessionId, CompleteSessionRequest request) {
    ClassSession session = findSessionById(sessionId);

    // FIXME :: 결제 선생 연동 후 입금날짜 기준으로 회차 다시 기록해야함, 현재로직은 이전과외일정 기준으로 회차를 설정하는데, 이전 과외 일정이 없거나 신규 과외는 회차가 기록되지 않는 이슈가 있음
    ClassManagement classManagement = session.getClassManagement();
    ClassMatching classMatching = classManagement.getClassMatching();

    classSessionRepository
        .findFirstByClassManagementAndSessionDateBeforeAndCompletedTrueAndCancelFalseAndRoundIsNotNullOrderBySessionDateDesc(
            session.getClassManagement(), session.getSessionDate()
        )
        .ifPresentOrElse((prevSession)-> {
              Integer maxRoundNumber = classMatching.getApplicationForm().maxRoundNumber();
              Integer prevRound = prevSession.getRound();
              Integer newRound =  (prevRound >= maxRoundNumber) ? 1 : prevRound + 1;
          session.complete(request.classMinute(), request.understanding(), request.homework(), newRound);
        },
        ()-> session.complete(request.classMinute(), request.understanding(), request.homework(), 1));


    Hibernate.initialize(
        session.getClassManagement().getClassMatching().getTeacher().getTeacherInfo());
    return session;
  }

  public Pair<ClassSession,LocalDate> change(Long sessionId, LocalDate sessionDate, LocalTime start) {
    ClassSession session = findSessionById(sessionId);
    LocalDate beforeSessionDate = session.getSessionDate();

    session.changeDate(sessionDate, start);

    return Pair.of(session, beforeSessionDate);
  }

  private ClassSession findSessionById(Long sessionId) {
    return classSessionRepository
        .findById(sessionId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다"));
  }

  public void deleteSession(ClassManagement classManagement, LocalDate changeStartDate) {
    classSessionRepository
        .deleteByClassManagementAndCancelIsFalseAndCompletedIsFalseAndSessionDateIsGreaterThanEqual(
            classManagement, changeStartDate);
  }

  public List<ClassMatching> createSessionOf(
      Teacher teacher, boolean forceCreate, LocalDate changeStartDate) {
    List<ClassMatching> classMatchings = classMatchingGetService.getMatched(teacher);

    classMatchings.forEach(
        cm ->
            classManagementRepository
                .findWithSchedule(cm.getClassMatchingId())
                .ifPresent(it -> this.createSingleSessions(it, forceCreate, changeStartDate)));

    return classMatchings;
  }

  public void createSingleSessions(
      ClassManagement classManagement, boolean forceCreate, LocalDate changeStartDate) {
    log.info(">>> 과외 일정 생성 : classManagementId = {}, forceCreate = {}, changeStartDate = {}",
      classManagement.getClassManagementId(), forceCreate, changeStartDate);
    LocalDate today = LocalDate.now();
    LocalDate firstDayOfThisMonth = today.withDayOfMonth(1);
    List<ClassSchedule> schedules = classManagement.getSchedules();
    List<ClassSession> existingSessions =
        classSessionRepository.findByClassManagementAndSessionDateIsGreaterThanEqual(
            classManagement, firstDayOfThisMonth);

    if (!forceCreate) {
      Set<YearMonth> targetMonths =
          Set.of(
              YearMonth.from(today),
              YearMonth.from(today.plusMonths(1)),
              YearMonth.from(today.plusMonths(2)));

      Set<YearMonth> monthsWithExistingSessions =
          existingSessions.stream()
              .map(session -> YearMonth.from(session.getSessionDate()))
              .collect(Collectors.toSet());

      // 3개월 모두 이미 존재한다면 생성을 건너뜀
      if (monthsWithExistingSessions.containsAll(targetMonths)) {
        return;
      }
    }
    Map<LocalDate, ClassSession> existingSessionMap = mapSessionsByDate(existingSessions);

    List<ClassSession> newSessions =
        generateNewSessions(schedules, classManagement, existingSessionMap, today, changeStartDate);
    classSessionRepository.saveAll(newSessions);
  }

  public void updateRoundSequentially(Long sessionId) {
    List<ClassSession> sessions = classSessionRepository.findBySameClassManagementId(sessionId);
    
    if (sessions.isEmpty()) {
      return;
    }
    
    Integer maxRound = sessions.get(0).getMaxRound();
    
    Integer teacherCancelRound = 0;
    boolean afterTeacherCancel = false;
    boolean isNextAfterTeacherCancel = false;
    int roundCounter = 1;

    for (ClassSession session : sessions) {
      if (isTeacherCancel(session)) {
        // Teacher 취소: 기존 round 유지
        teacherCancelRound = getValidTeacherRound(session);
        afterTeacherCancel = true;
        isNextAfterTeacherCancel = true;
      } else if (isOtherCancel(session)) {
        // 다른 이유로 취소: round 순차 증가
        updateRound(session, teacherCancelRound + roundCounter, maxRound);
        roundCounter = getNextRoundCounter(roundCounter, teacherCancelRound + roundCounter, maxRound);
      } else if (afterTeacherCancel) {
        if (isNextAfterTeacherCancel) {
          // Teacher 취소 다음 세션: round = 0
          classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), 0);
          isNextAfterTeacherCancel = false;
        } else {
          // Teacher 취소 다다음 세션부터: round 순차 증가
          updateRound(session, teacherCancelRound + roundCounter, maxRound);
          roundCounter = getNextRoundCounter(roundCounter, teacherCancelRound + roundCounter, maxRound);
        }
      }
    }
  }
  
  private boolean isTeacherCancel(ClassSession session) {
    return session.isTodayCancel() && CancelReason.TEACHER.name().equals(session.getCancelReason());
  }
  
  private boolean isOtherCancel(ClassSession session) {
    return session.isTodayCancel() && !CancelReason.TEACHER.name().equals(session.getCancelReason());
  }
  
  private Integer getValidTeacherRound(ClassSession session) {
    Integer teacherRound = session.getTeacherRound();
    return teacherRound != null && teacherRound != 0 ? teacherRound : 0;
  }
  
  private void updateRound(ClassSession session, int newRound, Integer maxRound) {
    if (newRound <= maxRound) {
      classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), newRound);
    } else {
      classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), 1);
    }
  }
  
  private int getNextRoundCounter(int currentCounter, int newRound, Integer maxRound) {
    if (newRound <= maxRound) {
      return currentCounter + 1;
    } else {
      return 2; // 1부터 다시 시작하므로 다음은 2
    }
  }
}
