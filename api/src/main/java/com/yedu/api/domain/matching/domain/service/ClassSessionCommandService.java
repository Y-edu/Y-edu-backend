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
      LocalDate today) {
    return schedules.stream()
        .flatMap(schedule -> schedule.generateUpcomingDates(classManagement, today, 4).stream())
        .filter(session -> !existingSessionMap.containsKey(session.getSessionDate()))
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

  public void complete(Long sessionId, CompleteSessionRequest request) {
    ClassSession session = findSessionById(sessionId);

    session.complete(request.understanding(), request.homeworkPercentage());
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

  public void deleteSession(ClassManagement classManagement) {
    classSessionRepository.deleteByClassManagementAndCancelIsFalseAndCompletedIsFalse(
        classManagement);
  }

  public List<ClassMatching> createSessionOf(Teacher teacher) {
    List<ClassMatching> classMatchings = classMatchingGetService.getMatched(teacher);

    classMatchings.forEach(
        cm ->
            classManagementRepository
                .findWithSchedule(cm.getClassMatchingId())
                .ifPresent(this::createSingleSessions));

    return classMatchings;
  }

  private void createSingleSessions(ClassManagement classManagement) {
    LocalDate today = LocalDate.now();
    List<ClassSchedule> schedules = classManagement.getSchedules();

    List<ClassSession> existingSessions =
        classSessionRepository.findByClassManagementAndSessionDateIsGreaterThanEqual(
            classManagement, today);

    if (existingSessions.size() > 3) { // 3회이상 과외가 남아있다면 생성안함
      return;
    }

    Map<LocalDate, ClassSession> existingSessionMap = mapSessionsByDate(existingSessions);
    List<ClassSession> newSessions =
        generateNewSessions(schedules, classManagement, existingSessionMap, today);
    classSessionRepository.saveAll(newSessions);
  }
}
