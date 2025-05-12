package com.yedu.api.domain.teacher.application.usecase;

import com.yedu.api.domain.teacher.domain.service.TeacherGetService;
import com.yedu.cache.support.storage.KeyStorage;
import com.yedu.common.event.bizppurio.TeacherAvailableTimeUpdateRequestEvent;
import com.yedu.common.event.bizppurio.TeacherCompleteTalkChangeNoticeEvent;
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
}
