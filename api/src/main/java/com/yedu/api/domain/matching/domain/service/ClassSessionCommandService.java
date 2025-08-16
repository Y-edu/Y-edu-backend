package com.yedu.api.domain.matching.domain.service;

import com.yedu.api.domain.matching.application.dto.req.CompleteSessionRequest;
import com.yedu.api.domain.matching.domain.entity.constant.CancelReason;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSchedule;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.repository.ClassManagementRepository;
import com.yedu.api.domain.matching.domain.repository.ClassSessionRepository;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    // 주차 수에 따라 라운드 증가량 결정
    int weeklyFrequency = 1;  // 순차적으로 1씩 증가
    
    // 모든 스케줄의 날짜를 수집하여 정렬
    List<LocalDate> allDates = schedules.stream()
        .flatMap(schedule -> 
            Stream.iterate(
                Optional.ofNullable(changeStartDate)
                    .or(() -> Optional.ofNullable(classManagement.getFirstDay())
                        .map(firstDay -> firstDay.isBefore(today) ? today : firstDay))
                    .orElse(today),
                date -> !date.isAfter(today.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth())),
                date -> date.plusDays(1))
            .filter(date -> Day.byDate(date).equals(schedule.getDay()))
            .filter(it -> !existingSessionMap.containsKey(it)))
        .sorted()
        .toList();
    
    // 전체 날짜에 대해 순차적으로 라운드 할당
    // ClassManagement에서 maxRound 계산
    String classCount = classManagement.getClassMatching().getApplicationForm().getClassCount();
    Integer maxRound = switch (classCount) {
        case String count when count.contains("주 1회") -> 4;
        case String count when count.contains("주 2회") -> 8;
        case String count when count.contains("주 3회") -> 12;
        case String count when count.contains("주 4회") -> 16;
        case String count when count.contains("주 5회") -> 20;
        case String count when count.contains("주 6회") -> 24;
        case String count when count.contains("주 7회") -> 28;
        default -> 4;
    };
    
    // 기존 세션에서 다음 teacherRound 계산
    Integer nextTeacherRound = 1; // 기본값
    if (!existingSessionMap.isEmpty()) {
        ClassSession lastSession = existingSessionMap.values().stream()
            .max(Comparator.comparing(ClassSession::getSessionDate))
            .orElse(null);
        if (lastSession != null && lastSession.getTeacherRound() != null && lastSession.getTeacherRound() != 0) {
            nextTeacherRound = (lastSession.getTeacherRound() % maxRound) + 1;
        }
    }
    
    AtomicInteger globalCounter = new AtomicInteger(nextTeacherRound);
    
    return allDates.stream()
        .map(date -> {
            int currentRound = globalCounter.getAndUpdate(round -> 
                round >= maxRound ? 1 : round + weeklyFrequency
            );
            
            // 해당 날짜의 스케줄 찾기
            ClassSchedule schedule = schedules.stream()
                .filter(s -> Day.byDate(date).equals(s.getDay()))
                .findFirst()
                .orElseThrow();
            
            return ClassSession.builder()
                .classManagement(classManagement)
                .sessionDate(date)
                .classTime(schedule.getClassTime())
                .completed(false)
                .cancel(false)
                .remind(false)
                .teacherRound(currentRound)
                .maxRound(maxRound)
                .build();
        })
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
    System.out.println("newSessions: " + newSessions);
    classSessionRepository.saveAll(newSessions);
  }

  public void updateRoundSequentially(Long sessionId) {
    List<ClassSession> sessions = classSessionRepository.findBySameClassManagementId(sessionId);
    
    if (sessions.isEmpty()) {
      return;
    }
    
    // 날짜순으로 정렬
    sessions.sort(Comparator.comparing(ClassSession::getSessionDate));
    
    Integer maxRound = sessions.get(0).getMaxRound();
    int currentRound = 1;
    int todayCancelTeacherRound = 0;
    for (ClassSession session : sessions) {
     
      if (session.isTodayCancel() && CancelReason.TEACHER.name().equals(session.getCancelReason())) {
        todayCancelTeacherRound = session.getTeacherRound();
        classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), 0);
      } else {
        // 정상 세션은 순차적으로 teacherRound 설정
        if (todayCancelTeacherRound != 0) {
          currentRound = todayCancelTeacherRound;
          classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), todayCancelTeacherRound);
          todayCancelTeacherRound = 0;
        } else {
          classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), currentRound);
          currentRound++;
        }
        
        if (currentRound > maxRound) {
          currentRound = 1; // maxRound 초과시 1로 초기화
        }
      }
    }
    
    // TEACHER 취소 다음 세션을 0으로 설정
    for (int i = 0; i < sessions.size() - 1; i++) {
      ClassSession currentSession = sessions.get(i);
      ClassSession nextSession = sessions.get(i + 1);
      
      if (currentSession.isTodayCancel() && 
          CancelReason.TEACHER.name().equals(currentSession.getCancelReason())) {
        // 다음 세션의 teacherRound를 0으로 설정
        classSessionRepository.updateRoundBySessionId(nextSession.getClassSessionId(), 0);
      }
    }
  }
  
  // 일반 휴강은 teacherRound 현재 회차만 0으로 처리하고, 그뒤 회차들은 +1씩 증가하고, maxRound보다 커지면 1로 초기화
  public void updateRoundForGeneralCancel(Long sessionId) {
    List<ClassSession> sessions = classSessionRepository.findBySameClassManagementId(sessionId);
    
    if (sessions.isEmpty()) {
      return;
    }

    // 1. 현재 sessionId의 teacherRound를 조회하여 변수에 저장
    ClassSession targetSession = sessions.stream()
        .filter(s -> s.getClassSessionId().equals(sessionId))
        .findFirst()
        .orElse(null);
    
    if (targetSession == null) {
      return;
    }
    
    Integer originalTeacherRound = targetSession.getTeacherRound();
    
    // 2. teacherRound를 0으로 업데이트
    classSessionRepository.updateRoundBySessionId(sessionId, 0);

    Integer maxRound = sessions.get(0).getMaxRound();
    // 3. 다음 session 데이터부터는 저장된 변수로 업데이트하고 +1씩 증가
    boolean foundSessionId = false;
    boolean isFirstAfterSessionId = true;
    
    for (ClassSession session : sessions) {
      if (session.getClassSessionId() == sessionId) {
        foundSessionId = true;  // sessionId를 찾았음
        continue;
      }
      
      if (foundSessionId) {  // sessionId 이후의 세션만 처리
        if (isFirstAfterSessionId) {
          // 첫 번째 세션: originalTeacherRound 그대로 사용
          classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), originalTeacherRound);
          isFirstAfterSessionId = false;
        } else {
          // 두 번째 세션부터: +1씩 증가
          int nextRound = originalTeacherRound + 1;
          if (nextRound > maxRound) {
            nextRound = 1;  // maxRound보다 커지면 1로 초기화
          }
          classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), nextRound);
          originalTeacherRound = nextRound;  // 다음 세션을 위해 업데이트
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
