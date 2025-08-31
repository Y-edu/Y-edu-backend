package com.yedu.api.domain.teacher.application.usecase;

import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormChangeRequest;
import com.yedu.api.domain.parents.domain.entity.SessionChangeForm;
import com.yedu.api.domain.parents.domain.entity.constant.SessionChangeType;
import com.yedu.api.domain.parents.domain.job.TeacherChangeJob;
import com.yedu.api.domain.parents.domain.repository.SessionChangeFormRepository;
import com.yedu.scheduling.support.ScheduleService;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TeacherChangeUsecase {

  private final SessionChangeFormRepository sessionChangeFormRepository;
  private final ScheduleService scheduleService;
  private final TeacherChangeJob teacherChangeJob;

  public void change(ApplicationFormChangeRequest request) {
    log.info(">>> 선생님 교체 신청 :{}", request);
    SessionChangeForm changeForm = sessionChangeFormRepository
        .findFirstByParents_PhoneNumberAndChangeTypeOrderByCreatedAtDesc(
            request.phoneNumber(),
            SessionChangeType.CHANGE_TEACHER
        )
        .orElseThrow(() -> new IllegalArgumentException("제출된 선생님 교체 신청건이 존재하지 않습니다"));

    ClassSession lastSession = changeForm.getLastSessionBeforeChange();
    long classMatchingId = lastSession
        .getClassManagement()
        .getClassMatching()
        .getClassMatchingId();

    LocalDateTime executeTime = LocalDateTime.of(
        lastSession.getSessionDate(),
        lastSession.getClassTime().finishTime()
    );
    JobDataMap jobDataMap = new JobDataMap(Map.of(
        "id", classMatchingId,
        "request", request)
    );
    if (executeTime.isBefore(LocalDateTime.now())) {
      teacherChangeJob.executeNow(jobDataMap);
      return;
    }

    String jobName = "job_change_teacher_%s".formatted(classMatchingId);
    scheduleService.schedule(
        executeTime,
        TeacherChangeJob.class,
        jobDataMap,
        jobName
    );
  }
}
