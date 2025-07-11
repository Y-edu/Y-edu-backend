package com.yedu.api.domain.matching.application.usecase;

import static com.yedu.api.domain.matching.application.constant.RefuseReason.UNABLE_DISTRICT;
import static com.yedu.api.domain.matching.application.constant.RefuseReason.UNABLE_NOW;
import static com.yedu.api.global.event.mapper.BizppurioEventMapper.*;

import com.yedu.api.domain.matching.application.dto.req.ClassMatchingRefuseRequest;
import com.yedu.api.domain.matching.application.mapper.ClassMatchingMapper;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.api.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.api.domain.matching.domain.service.ClassManagementCommandService;
import com.yedu.api.domain.matching.domain.service.ClassManagementQueryService;
import com.yedu.api.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.api.domain.matching.domain.service.ClassMatchingSaveService;
import com.yedu.api.domain.matching.domain.service.ClassMatchingUpdateService;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.service.TeacherGetService;
import com.yedu.api.domain.teacher.domain.service.TeacherUpdateService;
import com.yedu.api.global.exception.matching.MatchingStatusException;
import com.yedu.cache.support.dto.TeacherNotifyApplicationFormDto;
import com.yedu.cache.support.storage.ClassManagementTokenStorage;
import com.yedu.cache.support.storage.ResponseRateStorage;
import com.yedu.cache.support.storage.TeacherNotifyApplicationFormKeyStorage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassMatchingManageUseCase {
  private final ClassMatchingSaveService classMatchingSaveService;
  private final ClassMatchingGetService classMatchingGetService;
  private final ClassMatchingUpdateService classMatchingUpdateService;
  private final ClassManagementQueryService classManagementQueryService;
  private final ClassManagementCommandService classManagementCommandService;
  private final ResponseRateStorage responseRateStorage;
  private final TeacherUpdateService teacherUpdateService;
  private final ApplicationEventPublisher eventPublisher;
  private final TeacherNotifyApplicationFormKeyStorage teacherNotifyApplicationFormKeyStorage;
  private final ClassManagementTokenStorage classManagementTokenStorage;
  private final ClassMatchingRepository classMatchingRepository;
  private final TeacherGetService teacherGetService;

  public List<ClassMatching> saveAllClassMatching(
      List<Teacher> teachers, ApplicationForm applicationForm) {
    List<ClassMatching> classMatchings =
        teachers.stream()
            .map(
                teacher -> {
                  responseRateStorage.cache(teacher.getTeacherId());
                  teacherUpdateService.plusRequestCount(teacher);
                  return ClassMatchingMapper.mapToClassMatching(teacher, applicationForm);
                })
            .toList();
    classMatchings.forEach(classMatchingSaveService::save);
    return classMatchings;
  }

  public void refuseClassMatching(String token, ClassMatchingRefuseRequest request) {

    TeacherNotifyApplicationFormDto dto = teacherNotifyApplicationFormKeyStorage.get(token);
    ClassMatching classMatching = classMatchingGetService.getById(dto.matchingId());
    if (!classMatching.isWaiting())
      throw new MatchingStatusException(classMatching.getClassMatchingId());
    classMatchingUpdateService.updateRefuse(classMatching, request);
    Teacher teacher = classMatching.getTeacher();
    String refuseReason = request.refuseReason();
    sendBizppurioMessage(teacher, refuseReason);

    plusResponseCount(teacher.getTeacherId(), teacher);
  }

  private void sendBizppurioMessage(Teacher teacher, String refuseReason) {
    if (refuseReason.equals(UNABLE_NOW.getReason())) {
      teacherUpdateService.plusRefuseCount(teacher);
      eventPublisher.publishEvent(mapToMatchingRefuseCaseNowEvent(teacher));
      return;
    }
    if (refuseReason.equals(UNABLE_DISTRICT.getReason())) {
      teacherUpdateService.plusRefuseCount(teacher);
      eventPublisher.publishEvent(mapToMatchingRefuseCaseDistrictEvent(teacher));
      return;
    }
    teacherUpdateService.clearRefuseCount(teacher);
    eventPublisher.publishEvent(mapToMatchingRefuseCaseEvent(teacher));
  }

  public void acceptClassMatching(String token) {
    TeacherNotifyApplicationFormDto dto = teacherNotifyApplicationFormKeyStorage.get(token);
    ClassMatching classMatching = classMatchingGetService.getById(dto.matchingId());

    if (!classMatching.isWaiting())
      throw new MatchingStatusException(classMatching.getClassMatchingId());
    classMatchingUpdateService.updateAccept(classMatching);

    Teacher teacher = classMatching.getTeacher();
    teacherUpdateService.clearRefuseCount(teacher);
    eventPublisher.publishEvent(mapToMatchingAcceptCaseEvent(classMatching));

    plusResponseCount(teacher.getTeacherId(), teacher);
  }

  private void plusResponseCount(long teacherId, Teacher teacher) {
    if (!responseRateStorage.has(teacherId)) {
      return;
    }
    teacherUpdateService.plusResponseCount(teacher);
  }

  public void remindClassMatching() {
    classManagementQueryService.query().stream()
        .map(
            classManagement -> {
              String token =
                  classManagementTokenStorage.get(classManagement.getClassManagementId());
              classManagementCommandService.completeRemind(classManagement);
              return mapToTeacherClassRemindEvent(classManagement, token);
            })
        .forEach(eventPublisher::publishEvent);
  }

  public void updateMatching(List<Long> matchingIds, MatchingStatus matchingStatus) {
    List<ClassMatching> matchings = classMatchingRepository.findAllById(matchingIds);

    matchings.forEach(it -> it.update(matchingStatus));
  }

  public void changeTeacher(Long matchingId, Long newTeacherId) {
    ClassMatching matching = classMatchingRepository.findById(matchingId).orElseThrow();
    Teacher newTeacher = teacherGetService.byId(newTeacherId);

    matching.changeTeacher(newTeacher);
  }
}
