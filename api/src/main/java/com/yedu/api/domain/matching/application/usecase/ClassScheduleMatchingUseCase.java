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
import com.yedu.api.domain.matching.application.dto.res.RetrieveSessionDateResponse;
import com.yedu.api.domain.matching.application.dto.res.SessionResponse;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSchedule;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.entity.MatchingTimetable;
import com.yedu.api.domain.matching.domain.service.ClassManagementCommandService;
import com.yedu.api.domain.matching.domain.service.ClassManagementQueryService;
import com.yedu.api.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.api.domain.matching.domain.service.ClassSessionCommandService;
import com.yedu.api.domain.matching.domain.service.ClassSessionQueryService;
import com.yedu.api.domain.matching.domain.service.MatchingTimetableQueryService;
import com.yedu.api.domain.matching.domain.vo.ClassTime;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherInfo;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import com.yedu.cache.support.storage.ClassManagementTokenStorage;
import com.yedu.cache.support.storage.KeyStorage;
import com.yedu.cache.support.storage.TokenStorage;
import com.yedu.sheet.support.SheetApi;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

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
  private final ClassManagementTokenStorage classManagementTokenStorage;

  private final ClassMatchingGetService classMatchingGetService;

  private final ClassManagementCommandService classManagementCommandService;

  private final ClassSessionCommandService classSessionCommandService;

  private final ClassSessionQueryService classSessionQueryService;

  private final ClassManagementQueryService classManagementQueryService;

  private final SheetApi sheetApi;

  public String schedule(ClassScheduleMatchingRequest request) {
    String classNotifyToken = matchingIdApplicationNotifyKeyStorage.get(request.classMatchingId());
    ClassManagement classManagement = managementCommandService.schedule(request);
    String classManagementToken =
        classManagementKeyStorage.storeAndGet(classManagement.getClassManagementId());
    classManagementTokenStorage.store(classManagement.getClassManagementId(), classManagementToken);
    List<MatchingTimetable> timetables =
        matchingTimetableQueryService.query(
            classManagement.getClassMatching().getClassMatchingId());

    eventPublisher.publishEvent(mapToParentsExchangeEvent(classManagement));
    eventPublisher.publishEvent(mapToParentsClassNoticeEvent(classManagement));
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
          eventPublisher.publishEvent(mapToClassGuideEvent(classManagement));
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
    Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "sessionDate"));
    if (request.classMatchingId() != null) {
      ClassMatching matching = classMatchingGetService.getById(request.classMatchingId());
      List<ClassMatching> matchings = classManagementCommandService.create(request, matching);

      return classSessionQueryService.query(matching, matchings, null, pageable);
    }
    ClassMatching matching = getClassMatchingByToken(request.token());
    List<ClassMatching> matchings = classManagementCommandService.create(request, matching);

    return classSessionQueryService.query(matching, matchings, null, pageable);
  }

  public List<RetrieveScheduleResponse> retrieveSchedules(String token) {
    ClassMatching selectedMatching = getClassMatchingByToken(token);

    return classMatchingGetService.getMatched(selectedMatching.getTeacher()).stream()
        .map(it -> retrieveSchedule(it, selectedMatching))
        .toList();
  }

  private RetrieveScheduleResponse retrieveSchedule(
      ClassMatching matching, ClassMatching selectedMatching) {
    boolean send = selectedMatching.getClassMatchingId() == matching.getClassMatchingId();

    ClassManagement classManagement =
        classManagementQueryService.queryWithSchedule(matching.getClassMatchingId()).orElse(null);
    if (classManagement == null || CollectionUtils.isEmpty(classManagement.getSchedules())) {
      return RetrieveScheduleResponse.empty(
          matching.getApplicationForm().getApplicationFormId(),
          matching.getClassMatchingId(),
          send);
    }

    Map<Day, List<ClassTime>> schedules =
        classManagement.getSchedules().stream()
            .collect(
                Collectors.groupingBy(
                    ClassSchedule::getDay,
                    Collectors.mapping(ClassSchedule::getClassTime, Collectors.toList())));

    List<ClassSession> sessions = classSessionQueryService.query(classManagement);

    Map<LocalDate, Boolean> sessionReviewMap =
        sessions.stream()
            .collect(
                Collectors.toMap(
                    ClassSession::getSessionDate,
                    ClassSession::isCompleted,
                    (existing, replacement) -> existing || replacement // 하나라도 true면 true
                    ));
    LocalDate now = LocalDate.now();
    Map<String, List<LocalDate>> weekDatesMap = new HashMap<>();
    WeekFields weekFields = WeekFields.of(Locale.KOREA);
    Month baseMonth = now.getMonth();

    for (Entry<Day, List<ClassTime>> entry : schedules.entrySet()) {
      Day day = entry.getKey();
      DayOfWeek dayOfWeek = day.to();

      // 기준일: 이번 달 시작 주의 해당 요일부터 시작
      LocalDate targetDate = now.with(TemporalAdjusters.nextOrSame(dayOfWeek));

      while (targetDate.getMonth() == baseMonth) {
        int week = targetDate.get(weekFields.weekOfWeekBasedYear());
        int year = targetDate.getYear();
        String weekKey = year + "-" + week;

        weekDatesMap.computeIfAbsent(weekKey, k -> new ArrayList<>()).add(targetDate);

        targetDate = targetDate.plusWeeks(1L);
      }
      int week = targetDate.get(weekFields.weekOfWeekBasedYear());
      int year = targetDate.getYear();
      String weekKey = year + "-" + week;

      weekDatesMap.computeIfAbsent(weekKey, k -> new ArrayList<>()).add(targetDate);
    }

    // 주차별로 리뷰 있는지 확인
    Set<LocalDate> filteredDates = new HashSet<>();

    for (Map.Entry<String, List<LocalDate>> weekEntry : weekDatesMap.entrySet()) {
      List<LocalDate> datesInWeek = weekEntry.getValue();

      boolean hasReview =
          datesInWeek.stream().anyMatch(d -> sessionReviewMap.getOrDefault(d, false));

      if (!hasReview) {
        filteredDates.addAll(datesInWeek);
      }
    }

    return new RetrieveScheduleResponse(
        matching.getApplicationForm().getApplicationFormId(),
        matching.getClassMatchingId(),
        send,
        schedules,
        filteredDates.stream().toList());
  }

  public SessionResponse retrieveSession(String token, Pageable pageable, Boolean isComplete) {
    ClassMatching matching = getClassMatchingByToken(token);
    ClassManagement classManagement =
        classManagementQueryService.query(matching.getClassMatchingId()).orElse(null);

    if (classManagement == null) {
      return SessionResponse.empty();
    }

    Teacher teacher = matching.getTeacher();
    List<ClassMatching> matchings =
        classSessionCommandService.createSessionOf(teacher, false, null);

    return classSessionQueryService.query(matching, matchings, isComplete, pageable);
  }

  public void changeSessionDate(Long sessionId, ChangeSessionDateRequest request) {
    Pair<ClassSession, LocalDate> changeInfo = classSessionCommandService.change(sessionId,
        request.sessionDate(),
        request.start());
    ClassSession session = changeInfo.getKey();
    ClassManagement classManagement = session.getClassManagement();
    ClassMatching matching = classManagement.getClassMatching();
    TeacherInfo teacherInfo = matching.getTeacher().getTeacherInfo();

    // todo 날짜변경쉬트에 쓰기
    sheetApi.write(
        List.of(
            List.of(
                matching.getApplicationForm().getApplicationFormId(),
                teacherInfo.getNickName(),
                teacherInfo.getName(),
                teacherInfo.getPhoneNumber(),
                changeInfo.getValue().toString(), // 변경전 날짜
                session.getSessionDate().toString(), // 변경한 날짜
                session.getClassTime().getStart().toString(),
                session.getClassTime().getClassMinute().toString(),
                LocalDateTime.now().toString())), "change"
    );
  }

  public void cancelSession(Long sessionId, String cancelReason) {
    ClassSession session = classSessionCommandService.cancel(sessionId, cancelReason);
    ClassManagement classManagement = session.getClassManagement();
    ClassMatching matching = classManagement.getClassMatching();
    TeacherInfo teacherInfo = matching.getTeacher().getTeacherInfo();

    sheetApi.write(
        List.of(
            List.of(
                matching.getApplicationForm().getApplicationFormId(),
                teacherInfo.getNickName(),
                teacherInfo.getName(),
                teacherInfo.getPhoneNumber(),
                session.getSessionDate().toString(), // 여기 수정
                session.getClassTime().getStart().toString(),
                session.getClassTime().getClassMinute().toString(),
                "휴강",
                Optional.ofNullable(session.getCancelReason()).orElse(""),
                LocalDateTime.now().toString())), "cancel"
        );
  }

  public void revertCancelSession(Long sessionId) {
    ClassSession session = classSessionCommandService.revertCancel(sessionId);
    ClassManagement classManagement = session.getClassManagement();
    ClassMatching matching = classManagement.getClassMatching();
    TeacherInfo teacherInfo = matching.getTeacher().getTeacherInfo();

    sheetApi.write(
        List.of(
            List.of(
                matching.getApplicationForm().getApplicationFormId(),
                teacherInfo.getNickName(),
                teacherInfo.getName(),
                teacherInfo.getPhoneNumber(),
                session.getSessionDate().toString(), // 여기 수정
                session.getClassTime().getStart().toString(),
                session.getClassTime().getClassMinute().toString(),
                "휴강 취소",
                "",
                LocalDateTime.now().toString())), "cancel");
  }

  public void completeSession(Long sessionId, CompleteSessionRequest request) {
    ClassSession session = classSessionCommandService.complete(sessionId, request);
    ClassManagement classManagement = session.getClassManagement();
    ClassMatching matching = classManagement.getClassMatching();
    TeacherInfo teacherInfo = matching.getTeacher().getTeacherInfo();

    sheetApi.write(
        List.of(
            List.of(
                matching.getApplicationForm().getApplicationFormId(),
                teacherInfo.getNickName(),
                teacherInfo.getName(),
                teacherInfo.getPhoneNumber(),
                session.getSessionDate().toString(), // 여기 수정
                Optional.ofNullable(session.getRound()).map(String::valueOf).orElse(""), // 여기 수정
                session.getClassTime().getStart().toString(),
                session.getClassTime().getClassMinute().toString(),
                Optional.ofNullable(session.getRealClassTime()).map(String::valueOf).orElse(""),
                Optional.ofNullable(session.getHomework()).orElse(""),
                Optional.ofNullable(session.getUnderstanding()).orElse(""),
                LocalDateTime.now().toString())),"data");
  }

  public void completeSessionByToken(CompleteSessionTokenRequest request) {
    Long sessionId = classSessionKeyStorage.get(request.token());

    this.completeSession(
        sessionId,
        new CompleteSessionRequest(
            request.classMinute(), request.understanding(), request.homework()));
  }

  public RetrieveSessionDateResponse retrieveSessionDateByToken(String token) {
    Long sessionId = classSessionKeyStorage.get(token);
    if (sessionId == null) {
      return RetrieveSessionDateResponse.empty();
    }

    return classSessionQueryService.querySessionDate(sessionId);
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
