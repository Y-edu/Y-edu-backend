package com.yedu.api.domain.teacher.application.usecase;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.repository.ClassSessionRepository;
import com.yedu.api.domain.matching.domain.service.ClassManagementQueryService;
import com.yedu.api.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.api.domain.matching.domain.service.ClassSessionCommandService;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.service.TeacherGetService;
import com.yedu.cache.support.storage.CacheStorage;
import com.yedu.cache.support.storage.KeyStorage;
import com.yedu.common.event.bizppurio.TeacherAvailableTimeUpdateRequestEvent;
import com.yedu.common.event.bizppurio.TeacherCompleteTalkChangeNoticeEvent;
import com.yedu.common.event.bizppurio.TeacherWithNoScheduleCompleteTalkEvent;
import com.yedu.common.event.bizppurio.TeacherWithScheduleCompleteTalkEvent;
import com.yedu.common.event.bizppurio.TeacherWithScheduleCompleteTalkMonthlyRemindEvent;
import com.yedu.common.event.bizppurio.TeacherWithScheduleCompleteTalkRemindEvent;
import com.yedu.common.event.bizppurio.TeacherWithScheduleCompleteTalkWeeklyRemindEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = false)
@Slf4j
public class TeacherBatchUseCase {

  private final TeacherGetService teacherGetService;

  private final ApplicationEventPublisher eventPublisher;

  private final KeyStorage<Long> updateAvailableTimeKeyStorage;

  private final ClassSessionRepository classSessionRepository;

  private final CacheStorage classSessionStorage;

  private final KeyStorage<Long> classMatchingKeyStorage;

  private final KeyStorage<Long> classSessionKeyStorage;
  private final ClassMatchingGetService classMatchingGetService;
  private final ClassManagementQueryService classManagementQueryService;
  private final ClassSessionCommandService classSessionCommandService;
  private final ApplicationEventPublisher applicationEventPublisher;

  public void remindAvailableTime() {
    teacherGetService.emptyAvailableTime().stream()
        .map(
            teacher ->
                new TeacherAvailableTimeUpdateRequestEvent(
                    teacher.getTeacherInfo().getNickName(),
                    updateAvailableTimeKeyStorage.storeAndGet(teacher.getTeacherId()),
                    teacher.getTeacherInfo().getPhoneNumber()))
        .forEach(eventPublisher::publishEvent);
  }

  public void completeTalkNotice(Set<Long> teacherIds) {
    teacherIds.forEach(it -> {
      Teacher teacher;
      try{
         teacher = teacherGetService.byId(it);
      }catch (Exception e){
        log.error("선생님 찾기 중 오류 발생 : {} , id : {}", e.getMessage(), it);
        return;
      }
      String phoneNumber = teacher.getTeacherInfo().getPhoneNumber();
      TeacherCompleteTalkChangeNoticeEvent event = new TeacherCompleteTalkChangeNoticeEvent(phoneNumber);
      eventPublisher.publishEvent(event);
    });
  }


  public void remind() {
    sendCompleteTalkEvent(true);
  }

  public void completeTalkToTeacherWithSchedule() {
    sendCompleteTalkEvent(false);
  }

  private void sendCompleteTalkEvent(boolean isRemind) {
    teacherGetService
        .activeTeachers()
        .stream()
        .forEach(
            teacher -> {
              List<ClassMatching> matchings = classMatchingGetService.getMatched(teacher);
              matchings.stream()
                  .map(
                      matching ->
                          classManagementQueryService.queryWithSchedule(
                              matching.getClassMatchingId()))
                  .filter(Optional::isPresent)
                  .map(Optional::get)
                  .filter(ClassManagement::hasSchedule)
                  .filter(it -> !classSessionRepository.existsClassSessionByClassManagement(it))
                  .filter(it-> !isRemind) // remind가 아닌 경우에만 세션 생성
                  .forEach(it -> {
                    log.info(" >>> 배치 과외 일정 생성 시작 - classManagementId: {}", it.getClassManagementId());
                    classSessionCommandService.createSingleSessions(it, false, null);
                  });
            });

    LocalDateTime now = LocalDateTime.now();
    List<ClassSession> sessions =
        classSessionRepository
            .findBySessionDateAndCancelIsFalseAndCompletedIsFalse(now.toLocalDate())
            .stream()
            .filter(it -> !it.isRemind())
            .filter(
                it ->
                    it.isFinishAndNotComplete(now, isRemind)
                        && (isRemind || !classSessionStorage.has(it.getClassSessionId())))
            .toList();

    log.info(">>> 발송 후보 과외 일정 : {}", sessions);
    sessions.forEach(
        it -> {
          ClassMatching matching = it.getClassManagement().getClassMatching();
          if (matching.isNotInProgessStatus() || it.isRemind() || it.isCancel()) {
            return;
          }
          ApplicationForm applicationForm = matching.getApplicationForm();
          Teacher teacher = matching.getTeacher();
          String completeToken = classSessionKeyStorage.storeAndGet(it.getClassSessionId());
          String changeSessionToken =
              classMatchingKeyStorage.storeAndGet(matching.getClassMatchingId());

          log.info(">>>> 이벤트 발행 session Id : {} / 리마인드 여부 : {} / 선생님 ID : {} / 닉네임 : {} / 과외 일정 : {}", it.getClassSessionId(), isRemind, teacher.getTeacherId(), teacher.getTeacherInfo().getNickName(), it);
          if (isRemind) {
            it.remind();
            eventPublisher.publishEvent(
                new TeacherWithScheduleCompleteTalkRemindEvent(
                    applicationForm.getApplicationFormId(),
                    teacher.getTeacherInfo().getPhoneNumber(),
                    it.getSessionDate(),
                    completeToken,
                    changeSessionToken));
          } else {
            eventPublisher.publishEvent(
                new TeacherWithScheduleCompleteTalkEvent(
                    applicationForm.getApplicationFormId(),
                    teacher.getTeacherInfo().getPhoneNumber(),
                    it.getSessionDate(),
                    completeToken,
                    changeSessionToken));
          }

          // 캐시 여부에 상관없이 세션을 캐시 처리
          classSessionStorage.cache(it.getClassSessionId());
        });
  }

  public void completeTalkToTeacherWithNoSchedule() {
    teacherGetService.activeTeachers().stream()
        .flatMap(
            teacher -> {
              List<ClassMatching> matchings = classMatchingGetService.getMatched(teacher);
              return matchings.stream()
                  .map(
                      matching -> {
                        Optional<ClassManagement> management =
                            classManagementQueryService.queryWithSchedule(
                                matching.getClassMatchingId());

                        if (management.isPresent() && management.get().hasSchedule()) {
                          return null;
                        }

                        String token =
                            classMatchingKeyStorage.storeAndGet(matching.getClassMatchingId());

                        return new TeacherWithNoScheduleCompleteTalkEvent(
                            matching.getApplicationForm().getApplicationFormId(),
                            teacher.getTeacherInfo().getPhoneNumber(),
                            token);
                      });
            })
        .filter(Objects::nonNull)
        .forEach(eventPublisher::publishEvent);
  }


  @Scheduled(cron = "0 */5 * * * *")
  public void remindCompleteTalkWeekly() {
    log.info(">>> 주간 리마인드 알림톡 전송 시작");
    LocalDateTime now = LocalDateTime.now();

    List<ClassSession> sessionsToSend = classSessionRepository
        .findAllByRemindIsTrueAndCompletedIsFalseAndCancelIsFalse()
        .stream()
        .filter(it -> {
          LocalDateTime remindTime = LocalDateTime.of(it.getSessionDate().plusDays(7), it.getClassTime().finishTime());
          return now.isAfter(remindTime) && !it.isWeeklyRemind();
        })
        .toList();

    Map<String, List<ClassSession>> sessionsByPhoneNumber = sessionsToSend.stream()
        .collect(Collectors.groupingBy(
            session -> session.getClassManagement().getClassMatching().getTeacher().getTeacherInfo().getPhoneNumber()
        ));

    sessionsByPhoneNumber.forEach((phoneNumber, sessions) -> {
      try {
        String token = classMatchingKeyStorage.storeAndGet(
            sessions.get(0).getClassManagement().getClassMatching().getClassMatchingId()
        );
        applicationEventPublisher.publishEvent(
            new TeacherWithScheduleCompleteTalkWeeklyRemindEvent(phoneNumber, token)
        );
        sessions.forEach(ClassSession::weeklyRemind);

      } catch (Exception e) {
        log.error("알림톡 발송 실패: phoneNumber={}, error={}", phoneNumber, e.getMessage());
      }
    });

    log.info(">>> 주간 리마인드 알림톡 전송 완료");
  }

  @Scheduled(cron = "0 0 20 L * ?")
  public void remindCompleteTalkMontly() {
    log.info(">>> 월간 리마인드 알림톡 전송 시작");

    Map<String, String> teacherPhoneNumberByToken = classSessionRepository
        .findAllByRemindIsTrueAndCompletedIsFalseAndCancelIsFalse()
        .stream()
        .collect(Collectors.toMap(
            session -> classMatchingKeyStorage.storeAndGet(
                session.getClassManagement().getClassMatching().getClassMatchingId()
            ),
            session -> session.getClassManagement().getClassMatching().getTeacher().getTeacherInfo().getPhoneNumber(),
            (existing, replacement) -> existing
        ));

    teacherPhoneNumberByToken.forEach((String token, String phoneNumber) -> applicationEventPublisher.publishEvent(
        new TeacherWithScheduleCompleteTalkMonthlyRemindEvent(
            phoneNumber,
            token
        )
    ));
    log.info(">>> 월간 리마인드 알림톡 전송 완료");
  }
}
