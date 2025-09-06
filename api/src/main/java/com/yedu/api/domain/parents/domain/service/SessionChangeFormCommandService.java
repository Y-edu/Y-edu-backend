package com.yedu.api.domain.parents.domain.service;

import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.repository.ClassSessionRepository;
import com.yedu.api.domain.parents.application.dto.req.AcceptChangeSessionRequest;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.parents.domain.entity.SessionChangeForm;
import com.yedu.api.domain.parents.domain.entity.constant.SessionChangeType;
import com.yedu.api.domain.parents.domain.job.MatchingStatusPauseJob;
import com.yedu.api.domain.parents.domain.repository.SessionChangeFormRepository;
import com.yedu.scheduling.support.ScheduleService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class SessionChangeFormCommandService {

  private final SessionChangeFormRepository sessionChangeFormRepository;
  private final ClassSessionRepository classSessionRepository;
  private final ScheduleService scheduleService;

  public ClassSession save(Parents parents, AcceptChangeSessionRequest request) {
    ClassSession lastSession = classSessionRepository.findById(request.sessionId()).orElseThrow();

    List<SessionChangeForm> previousForm =
        sessionChangeFormRepository.findByLastSessionBeforeChange_ClassManagementAndChangeType(
            lastSession.getClassManagement(), request.type());
    sessionChangeFormRepository.deleteAll(previousForm);

    SessionChangeForm sessionChangeForm =
        SessionChangeForm.builder()
            .lastSessionBeforeChange(lastSession)
            .parents(parents)
            .changeType(request.type())
            .build();

    sessionChangeFormRepository.save(sessionChangeForm);

    if (sessionChangeForm.getChangeType().equals(SessionChangeType.PAUSE)) {
      LocalTime lastTime = lastSession.getClassTime().finishTime();
      LocalDate lastDate = lastSession.getSessionDate();
      LocalDateTime executeTime = LocalDateTime.of(lastDate, lastTime);
      Long classMatchingId =
          lastSession.getClassManagement().getClassMatching().getClassMatchingId();

      String jobName = "job_pause_%s".formatted(classMatchingId);
      scheduleService.cancelScheduledJob(jobName);
      scheduleService.schedule(
          executeTime,
          MatchingStatusPauseJob.class,
          new JobDataMap(Map.of("id", classMatchingId)),
          jobName);
    }

    return lastSession;
  }
}
