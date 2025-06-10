package com.yedu.api.domain.matching.domain.service;

import com.yedu.api.domain.matching.application.dto.req.CompleteSessionRequest;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSchedule;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.repository.ClassManagementRepository;
import com.yedu.api.domain.matching.domain.repository.ClassSessionRepository;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  public void cancel(Long sessionId, String cancelReason) {
    ClassSession session = findSessionById(sessionId);

    session.cancel(cancelReason);
  }

  public void revertCancel(Long sessionId) {
    ClassSession session = findSessionById(sessionId);

    session.revertCancel();
  }

  public ClassSession complete(Long sessionId, CompleteSessionRequest request) {
    ClassSession session = findSessionById(sessionId);

    session.complete(request.classMinute(), request.understanding(), request.homework());

    Hibernate.initialize(
        session.getClassManagement().getClassMatching().getTeacher().getTeacherInfo());
    return session;
  }

  public void change(Long sessionId, LocalDate sessionDate, LocalTime start) {
    ClassSession session = findSessionById(sessionId);

    session.changeDate(sessionDate, start);
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
    LocalDate today = LocalDate.now();
    LocalDate firstDayOfThisMonth = today.withDayOfMonth(1);
    List<ClassSchedule> schedules = classManagement.getSchedules();
    List<ClassSession> existingSessions =
        classSessionRepository.findByClassManagementAndSessionDateIsGreaterThanEqual(
            classManagement, firstDayOfThisMonth);

    if (!forceCreate) {
//      boolean hasExistingSessionInThisMonth =
//          existingSessions.stream()
//              .anyMatch(
//                  it -> it.getSessionDate().getMonth().equals(firstDayOfThisMonth.getMonth()));
//
//      if (hasExistingSessionInThisMonth) {
//        return;
//      }
    }
    Map<LocalDate, ClassSession> existingSessionMap = mapSessionsByDate(existingSessions);

    List<ClassSession> newSessions =
        generateNewSessions(schedules, classManagement, existingSessionMap, today, changeStartDate);
    classSessionRepository.saveAll(newSessions);
  }
}
