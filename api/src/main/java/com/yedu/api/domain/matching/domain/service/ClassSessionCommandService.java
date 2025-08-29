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
import java.util.concurrent.atomic.AtomicReference;
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
    
    if (allDates.isEmpty()) {
        return List.of();
    }
    
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
    
    // 새로 생성되는 날짜들에서 월별로 teacherRound 관리
    AtomicInteger teacherRoundCounter = new AtomicInteger(1);
    AtomicReference<LocalDate> currentMonthRef = new AtomicReference<>();
    
    return allDates.stream()
        .map(date -> {
            // 월이 바뀌면 teacherRound를 1로 리셋
            LocalDate currentMonth = currentMonthRef.get();
            if (currentMonth == null || 
                date.getMonth() != currentMonth.getMonth() || 
                date.getYear() != currentMonth.getYear()) {
                teacherRoundCounter.set(1);
                currentMonthRef.set(date);
            }
            
            // 현재 월 내에서 순차적으로 증가
            int currentRound = teacherRoundCounter.getAndIncrement();
            
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
    ClassSession currentSession = findSessionById(sessionId);

    currentSession.revertCancel();

   // 2. 현재 회차의 sessionDate 변수 저장
   LocalDate currentSessionDate = currentSession.getSessionDate();
    
   // 3. classManagementId & sessionDate(DATE_FORMAT YYYYMM)와 일치하는 sesionDate, 무료 보강 제외한 조건인 classSessions 목록 조회
   LocalDate startOfMonth = currentSessionDate.withDayOfMonth(1);
   LocalDate endOfMonth = currentSessionDate.withDayOfMonth(currentSessionDate.lengthOfMonth());
   Long classManagementId = currentSession.getClassManagement().getClassManagementId();
   List<ClassSession> allClassSessions = classSessionRepository.findByClassManagementIdAndYearMonth(classManagementId, startOfMonth, endOfMonth);
   
   // 4. 순서대로 teacherRound 인덱스로 업데이트
   for (int i = 0; i < allClassSessions.size(); i++) {
    ClassSession session = allClassSessions.get(i);
    classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), i + 1);
  }

    return currentSession;
  }

  public ClassSession complete(Long sessionId, CompleteSessionRequest request) {
    ClassSession session = findSessionById(sessionId);

    // FIXME :: 결제 선생 연동 후 입금날짜 기준으로 회차 다시 기록해야함, 현재로직은 이전과외일정 기준으로 회차를 설정하는데, 이전 과외 일정이 없거나 신규 과외는 회차가 기록되지 않는 이슈가 있음
    ClassManagement classManagement = session.getClassManagement();
    ClassMatching classMatching = classManagement.getClassMatching();

    // 간단하게 1로 설정 (나중에 로직 개선 필요)
    session.complete(request.classMinute(), request.understanding(), request.homework(), 1);


    Hibernate.initialize(
        session.getClassManagement().getClassMatching().getTeacher().getTeacherInfo());
    return session;
  }

  public Pair<ClassSession,LocalDate> change(Long sessionId, LocalDate sessionDate, LocalTime start) {
    ClassSession session = findSessionById(sessionId);
    session.changeDate(sessionDate, start);

    return Pair.of(session, session.getSessionDate());
  }

  private ClassSession findSessionById(Long sessionId) {
    return classSessionRepository
        .findById(sessionId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다"));
  }

  public void deleteSession(ClassManagement classManagement, LocalDate changeStartDate) {
    classSessionRepository
        .deleteByClassManagementAndCancelIsFalseAndCompletedIsFalseAndSessionDateIsGreaterThanEqualAndTeacherRoundNot(
            classManagement, changeStartDate, 0);
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
    List<ClassSession> currentSessions = classSessionRepository.findBySameClassManagementId(sessionId);
    ClassSession currentSession = classSessionRepository.findById(sessionId).orElse(null);
   
    if (currentSessions.isEmpty() || currentSession == null) {
      return;
    }

    if (currentSession.getCancelReason() == CancelReason.TEACHER.name()) {
      // 현재 세션의 인덱스 찾기
      int currentIndex = -1;
      for (int i = 0; i < currentSessions.size(); i++) {
          if (currentSessions.get(i).getClassSessionId().equals(sessionId)) {
              currentIndex = i;
              break;
          }
      }
      System.out.println("currentIndex: " + currentIndex);
      System.out.println("currentSessions size: " + currentSessions.size());
      
      // 다음 세션 ID 저장
      Long nextSessionId = null;
      if (currentIndex != -1 && currentIndex + 1 < currentSessions.size()) {
          nextSessionId = currentSessions.get(currentIndex + 1).getClassSessionId();
          System.out.println("Found nextSessionId: " + nextSessionId);
      } else {
          System.out.println("No next session found. currentIndex: " + currentIndex + ", size: " + currentSessions.size());
      }
      
      System.out.println("nextSessionId: " + nextSessionId);
      
      // 1. 무료 보강 회차 업데이트 - nextSessionId가 null이 아닐 때만
      if (nextSessionId != null) {
          System.out.println("Updating nextSessionId: " + nextSessionId + " to teacherRound: 0");
          classSessionRepository.updateRoundBySessionId(nextSessionId, 0);
      } else {
          System.out.println("Skipping update because nextSessionId is null");
      }
  }
    // 2. 현재 회차의 sessionDate 변수 저장
    LocalDate currentSessionDate = currentSession.getSessionDate();
    
    // 3. TEACHER 취소 세션과 그 다음 세션의 teacherRound를 0으로 업데이트
    LocalDate startOfMonth = currentSessionDate.withDayOfMonth(1);
    LocalDate endOfMonth = currentSessionDate.withDayOfMonth(currentSessionDate.lengthOfMonth());
    Long classManagementId = currentSessions.get(0).getClassManagement().getClassManagementId();
    
    // 해당 월의 모든 세션 조회 (teacherRound 조건 없이)
    List<ClassSession> allSessionsInMonth = classSessionRepository.findByClassManagementAndSessionDateBetween(
        currentSessions.get(0).getClassManagement(), startOfMonth, endOfMonth);
    
    // TEACHER 취소 세션과 그 다음 세션의 teacherRound를 0으로 업데이트
    for (int i = 0; i < allSessionsInMonth.size(); i++) {
        ClassSession session = allSessionsInMonth.get(i);
        if (session.isTodayCancel() && "TEACHER".equals(session.getCancelReason())) {
            // TEACHER 취소 세션
            classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), 0);
            System.out.println("TEACHER 취소 세션 teacherRound를 0으로 설정: " + session.getClassSessionId());
            
            // 다음 세션도 0으로 설정 (다음 세션이 존재하는 경우)
            if (i + 1 < allSessionsInMonth.size()) {
                ClassSession nextSession = allSessionsInMonth.get(i + 1);
                classSessionRepository.updateRoundBySessionId(nextSession.getClassSessionId(), 0);
                System.out.println("TEACHER 취소 다음 세션 teacherRound를 0으로 설정: " + nextSession.getClassSessionId());
            }
        }
    }
    List<ClassSession> allClassSessions = classSessionRepository.findByClassManagementIdAndYearMonth(
        classManagementId, startOfMonth, endOfMonth);
    
    // 5. 해당 월 내에서 teacherRound 순차 업데이트
    if (!allClassSessions.isEmpty()) {
        // 날짜순으로 정렬
        allClassSessions.sort(Comparator.comparing(ClassSession::getSessionDate));
        
        int currentTeacherRound = 1;  // 해당 월은 1부터 시작
        
        for (ClassSession session : allClassSessions) {
            // teacherRound가 0이 아닌 세션들만 순차적으로 업데이트
            classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), currentTeacherRound);
            System.out.println("정상 세션 teacherRound 설정: " + session.getClassSessionId() + " -> " + currentTeacherRound);
            currentTeacherRound++;
        }
    }
  }
  
  // 특정 날짜 업데이트 후 정렬하여 teacherRound 순차 업데이트
  public void updateDateAndReorderRounds(Long sessionId) {
    List<ClassSession> sessions = classSessionRepository.findBySameClassManagementId(sessionId);
    
    if (sessions.isEmpty()) {
      return;
    }
  
    ClassSession currentSession = classSessionRepository.findById(sessionId).orElse(null);
    if (currentSession == null) {
      return;
    }

    // 요청한 날짜가 기존 월과 다른 월인지 확인
    LocalDate currentSessionDate = currentSession.getSessionDate();
    LocalDate requestedDate = sessions.stream()
        .filter(s -> s.getClassSessionId().equals(sessionId))
        .findFirst()
        .map(ClassSession::getSessionDate)
        .orElse(currentSessionDate);  // 요청된 날짜
    
    /** 현재 session_date의 월 업데이트 */
    LocalDate currentSessionDateStartOfMonth = currentSessionDate.withDayOfMonth(1);
    LocalDate currentSessionDateEndOfMonth = currentSessionDate.withDayOfMonth(currentSessionDate.lengthOfMonth());
    
    // 현재 월의 세션들 업데이트
    List<ClassSession> currentMonthSessions = classSessionRepository.findByClassManagementAndSessionDateBetween(
        currentSession.getClassManagement(), currentSessionDateStartOfMonth, currentSessionDateEndOfMonth);
    List<ClassSession> currentValidSessions = currentMonthSessions.stream()
        .filter(session -> session.getTeacherRound() != null && session.getTeacherRound() != 0)
        .sorted(Comparator.comparing(ClassSession::getSessionDate))
        .toList();

    if (!currentValidSessions.isEmpty()) {
        int currentTeacherRound = 1;  // 현재 월은 1부터 시작
        for (ClassSession session : currentValidSessions) {
            classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), currentTeacherRound);
            System.out.println("현재 월 정상 세션 teacherRound 설정: " + session.getClassSessionId() + " -> " + currentTeacherRound);
            currentTeacherRound++;
        }
    }

    // 월이 다른 경우에만 변경할 월의 세션들 업데이트
    if (currentSessionDate.getMonth() != requestedDate.getMonth() || 
        currentSessionDate.getYear() != requestedDate.getYear()) {
        
        /** 변경할 월의 세션들 업데이트 */
        LocalDate requestedSessionDateStartOfMonth = requestedDate.withDayOfMonth(1);
        LocalDate requestedSessionDateEndOfMonth = requestedDate.withDayOfMonth(requestedDate.lengthOfMonth());

        List<ClassSession> requestedMonthSessions = classSessionRepository.findByClassManagementAndSessionDateBetween(
            currentSession.getClassManagement(), requestedSessionDateStartOfMonth, requestedSessionDateEndOfMonth);
        List<ClassSession> requestedValidSessions = requestedMonthSessions.stream()
            .filter(session -> session.getTeacherRound() != null && session.getTeacherRound() != 0)
            .sorted(Comparator.comparing(ClassSession::getSessionDate))
            .toList();

        if (!requestedValidSessions.isEmpty()) {
            int requestedTeacherRound = 1;  // 변경할 월도 1부터 시작
            for (ClassSession session : requestedValidSessions) {
                classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), requestedTeacherRound);
                System.out.println("변경할 월 정상 세션 teacherRound 설정: " + session.getClassSessionId() + " -> " + requestedTeacherRound);
                requestedTeacherRound++;
            }
        }
    }
  }
  

  
  // 일반 휴강은 teacherRound 현재 회차만 0으로 처리하고, 그뒤 회차들은 +1씩 증가하고, maxRound보다 커지면 1로 초기화
  public void updateRoundForGeneralCancel(Long sessionId) {
    List<ClassSession> sessions = classSessionRepository.findBySameClassManagementId(sessionId);
    
    if (sessions.isEmpty()) {
      return;
    }

    ClassSession currentSession = classSessionRepository.findById(sessionId).orElse(null);
    if (currentSession == null) {
      return;
    }
    
    LocalDate currentSessionDate = currentSession.getSessionDate();
    LocalDate startOfMonth = currentSessionDate.withDayOfMonth(1);
    LocalDate endOfMonth = currentSessionDate.withDayOfMonth(currentSessionDate.lengthOfMonth());
    
    // 1. 먼저 취소된 세션의 teacherRound를 0으로 설정
    classSessionRepository.updateRoundBySessionId(sessionId, 0);
    System.out.println("취소된 세션 teacherRound를 0으로 설정: " + sessionId);
    
    // 2. 해당 월의 모든 세션을 한 번만 조회
    List<ClassSession> allSessionsInMonth = classSessionRepository.findByClassManagementAndSessionDateBetween(
        currentSession.getClassManagement(), startOfMonth, endOfMonth);
    
    // 3. 취소된 세션들의 teacherRound를 0으로 설정
    for (ClassSession session : allSessionsInMonth) {
        if (session.isCancel() && !session.isTodayCancel()) {
            classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), 0);
            System.out.println("일반 취소 세션 teacherRound를 0으로 설정: " + session.getClassSessionId());
        }
    }
    
    // 4. teacherRound가 0이 아닌 세션들만 필터링하여 순차적으로 1부터 업데이트
    List<ClassSession> validSessions = allSessionsInMonth.stream()
        .filter(session -> session.getTeacherRound() != null && session.getTeacherRound() != 0)
        .sorted(Comparator.comparing(ClassSession::getSessionDate))
        .toList();
    
    if (!validSessions.isEmpty()) {
        int currentTeacherRound = 1;  // 해당 월은 1부터 시작
        
        for (ClassSession session : validSessions) {
            classSessionRepository.updateRoundBySessionId(session.getClassSessionId(), currentTeacherRound);
            System.out.println("정상 세션 teacherRound 설정: " + session.getClassSessionId() + " -> " + currentTeacherRound);
            currentTeacherRound++;
      }
    }
  }
}
