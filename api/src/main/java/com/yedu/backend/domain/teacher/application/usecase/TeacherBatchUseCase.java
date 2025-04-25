package com.yedu.backend.domain.teacher.application.usecase;

import com.yedu.backend.domain.teacher.domain.service.TeacherGetService;
import com.yedu.backend.global.event.publisher.EventPublisher;
import com.yedu.cache.support.storage.KeyStorage;
import com.yedu.common.event.bizppurio.TeacherAvailableTimeUpdateRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class TeacherBatchUseCase {

  private final TeacherGetService teacherGetService;

  private final EventPublisher eventPublisher;

  private final KeyStorage<Long> updateAvailableTimeKeyStorage;

  public void remindAvailableTime() {
    teacherGetService.emptyAvailableTime().stream()
        .map(
            teacher ->
                new TeacherAvailableTimeUpdateRequestEvent(
                    teacher.getTeacherInfo().getNickName(),
                    updateAvailableTimeKeyStorage.storeAndGet(teacher.getTeacherId()),
                    teacher.getTeacherInfo().getPhoneNumber()))
        .forEach(eventPublisher::publishTeacherAvailableTimeUpdateRequestEvent);
  }
}
