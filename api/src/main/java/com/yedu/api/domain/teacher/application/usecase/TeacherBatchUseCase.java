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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

  public void completeTalkNotice() {
    teacherGetService.activeTeachers().stream()
        .map(
            teacher ->
                new TeacherCompleteTalkChangeNoticeEvent(teacher.getTeacherInfo().getPhoneNumber()))
        .forEach(eventPublisher::publishEvent);
  }

  public void remind() {
    sendCompleteTalkEvent(false);
  }

  public void completeTalkToTeacherWithSchedule() {
    sendCompleteTalkEvent(true);
  }

  private void sendCompleteTalkEvent(boolean checkCache) {
    teacherGetService
        .activeTeachers()
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
                  .forEach(it -> classSessionCommandService.createSingleSessions(it, false));
            });

    LocalDateTime now = LocalDateTime.now();
    List<ClassSession> sessions =
        classSessionRepository
            .findBySessionDateAndCancelIsFalseAndCompletedIsFalse(now.toLocalDate())
            .stream()
            .filter(
                it ->
                    it.isFinishAndNotComplete(now)
                        && (!checkCache || !classSessionStorage.has(it.getClassSessionId())))
            .toList();

    sessions.forEach(
        it -> {
          ClassMatching matching = it.getClassManagement().getClassMatching();
          ApplicationForm applicationForm = matching.getApplicationForm();
          Teacher teacher = matching.getTeacher();

          String completeToken = classSessionKeyStorage.storeAndGet(it.getClassSessionId());
          String changeSessionToken =
              classMatchingKeyStorage.storeAndGet(matching.getClassMatchingId());

          eventPublisher.publishEvent(
              new TeacherWithScheduleCompleteTalkEvent(
                  applicationForm.getApplicationFormId(),
                  teacher.getTeacherInfo().getPhoneNumber(),
                  it.getSessionDate(),
                  completeToken,
                  changeSessionToken));

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
}
