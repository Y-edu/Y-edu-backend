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
import com.yedu.common.event.bizppurio.TeacherWithScheduleCompleteTalkRemindEvent;
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

  private static final List<Long> TEACHER_COMPLETE_TALK_WHITE_LIST = List.of(
      22L,
      49L,
      52L
  );

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
    List.of(52L).forEach(it -> {
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
        .filter(teacher -> TEACHER_COMPLETE_TALK_WHITE_LIST.contains(teacher.getTeacherId()))
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
                  .forEach(it -> classSessionCommandService.createSingleSessions(it, false, null));
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

    sessions.forEach(
        it -> {
          ClassMatching matching = it.getClassManagement().getClassMatching();
          if (matching.isNotInProgessStatus() || it.isRemind()) {
            return;
          }
          ApplicationForm applicationForm = matching.getApplicationForm();
          Teacher teacher = matching.getTeacher();

          if (!TEACHER_COMPLETE_TALK_WHITE_LIST.contains(teacher.getTeacherId())){
            return;
          }

          String completeToken = classSessionKeyStorage.storeAndGet(it.getClassSessionId());
          String changeSessionToken =
              classMatchingKeyStorage.storeAndGet(matching.getClassMatchingId());

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
}
