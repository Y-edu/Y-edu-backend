package com.yedu.api.domain.matching.domain.service;

import com.yedu.api.domain.matching.application.dto.req.CompleteSessionRequest;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSchedule;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.entity.ClassSessions;
import com.yedu.api.domain.matching.domain.entity.constant.CancelReason;
import com.yedu.api.domain.matching.domain.entity.constant.PayStatus;
import com.yedu.api.domain.matching.domain.repository.ClassManagementRepository;
import com.yedu.api.domain.matching.domain.repository.ClassSessionRepository;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.payment.api.PaymentTemplate;
import com.yedu.payment.api.dto.SendBillRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
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
  private final PaymentTemplate paymentTemplate;
  @Value("${app.yedu.url}")
  public String serverUrl;

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

  public ClassSession cancel(Long sessionId, String cancelReason) {
    ClassSession session = findSessionById(sessionId);

    session.cancel(cancelReason);

    return session;
  }

  public ClassSession revertCancel(Long sessionId) {
    ClassSession session = findSessionById(sessionId);

    session.revertCancel();

    return session;
  }

  public ClassSession complete(Long sessionId, CompleteSessionRequest request) {
    ClassSession session = findSessionById(sessionId);

    ClassManagement classManagement = session.getClassManagement();
    ClassMatching classMatching = classManagement.getClassMatching();
    Integer maxRound = classManagement.getClassMatching().getApplicationForm()
        .maxRoundNumber();
    classSessionRepository
        .findFirstByClassManagementAndSessionDateBeforeOrderBySessionDateDesc(
            session.getClassManagement(), session.getSessionDate()
        )
        .ifPresentOrElse((prevSession)-> {
              Integer maxRoundNumber = classMatching.getApplicationForm().maxRoundNumber();
              Integer prevRound = prevSession.getRound();
              Integer newRound =  (prevRound >= maxRoundNumber) ? 1 : prevRound + 1;
          session.complete(request.classMinute(), request.understanding(), request.homework(), newRound);
        },
        ()-> session.complete(request.classMinute(), request.understanding(), request.homework(), 1));

    classSessionRepository
        .findAllByClassManagementAndSessionDateGreaterThanAndCompletedIsTrue(
            session.getClassManagement(), session.getSessionDate())
        .forEach(afterSession -> afterSession.increaseRound(maxRound));


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

  public void pay(List<Long> sessionIds) {
    LocalDateTime paidAt = LocalDateTime.now();
    classSessionRepository.findAllById(sessionIds).forEach(session-> session.payApprove(paidAt));
  }
}
