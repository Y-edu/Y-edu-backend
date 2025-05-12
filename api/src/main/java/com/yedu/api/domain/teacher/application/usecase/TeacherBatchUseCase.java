package com.yedu.api.domain.teacher.application.usecase;

import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.repository.ClassSessionRepository;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.service.TeacherGetService;
import com.yedu.cache.support.storage.CacheStorage;
import com.yedu.cache.support.storage.KeyStorage;
import com.yedu.common.event.bizppurio.TeacherAvailableTimeUpdateRequestEvent;
import com.yedu.common.event.bizppurio.TeacherCompleteTalkChangeNoticeEvent;
import com.yedu.common.event.bizppurio.TeacherWithScheduleCompleteTalkEvent;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class TeacherBatchUseCase {

  private final TeacherGetService teacherGetService;

  private final ApplicationEventPublisher eventPublisher;

  private final KeyStorage<Long> updateAvailableTimeKeyStorage;

  private final ClassSessionRepository classSessionRepository;

  private final CacheStorage classSessionStorage;

  private final KeyStorage<Long> classMatchingKeyStorage;

  private final KeyStorage<Long> classSessionKeyStorage;

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

  public void completeTalkToTeacherWithSchedule() {
    LocalDateTime now = LocalDateTime.now();

    List<ClassSession> sessions =
        classSessionRepository.findBySessionDate(now.toLocalDate()).stream()
            .filter(it -> it.isFinish(now) && !classSessionStorage.has(it.getClassSessionId()))
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
          classSessionStorage.cache(it.getClassSessionId());
        });
  }
}
