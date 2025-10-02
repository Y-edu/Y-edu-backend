package com.yedu.scheduling.support;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

  private final Scheduler scheduler;

  @SneakyThrows
  public <T extends Job> void schedule(LocalDateTime executeTime, Class<T> jobClass, JobDataMap jobDataMap, String jobName)  {
    JobKey jobKey = JobKey.jobKey(jobName);

    JobDetail jobDetail = JobBuilder.newJob(jobClass)
        .withIdentity(jobName)
        .usingJobData(jobDataMap)
        .requestRecovery(true)
        .storeDurably(true)
        .build();

    if (scheduler.checkExists(jobKey)) {
      log.warn("Job already exists: {}. Deleting existing job.", jobName);
      scheduler.deleteJob(jobKey);
    }

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(jobName + "_trigger")
        .startAt(Date.from(executeTime.atZone(ZoneId.systemDefault()).toInstant()))
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withRepeatCount(0)
            .withMisfireHandlingInstructionFireNow()
        )
        .build();

    scheduler.scheduleJob(jobDetail, trigger);
    log.info("Job scheduled: {} at {}", jobName, executeTime);
  }

  @SneakyThrows
  public <T extends Job> boolean cancelScheduledJob(String jobName) {
    JobKey jobKey = JobKey.jobKey(jobName);

    if (scheduler.checkExists(jobKey)) {
      log.warn("Job already exists: {}. Deleting existing job.", jobName);
      scheduler.deleteJob(jobKey);
    }

    if (!scheduler.checkExists(jobKey)) {
      log.warn("Job does not exist: {}", jobName);
      return false;
    }

    boolean deleted = scheduler.deleteJob(jobKey);
    if (deleted) {
      log.info("Job cancelled: {}", jobName);
      return true;
    }

    log.error("Failed to cancel job: {}", jobName);
    return false;
  }

}
