package com.yedu.scheduling.support;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

@Service
@RequiredArgsConstructor
public class ScheduleService {

  private final Scheduler scheduler;

  @SneakyThrows
  public <T extends Job> void schedule(LocalDateTime executeTime, Class<T> jobClass, JobDataMap jobDataMap, String jobName)  {
    JobDetail jobDetail = JobBuilder.newJob(jobClass)
        .withIdentity(jobName)
        .usingJobData(jobDataMap)
        .requestRecovery(true)
        .storeDurably(true)
        .build();

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(jobName + "_trigger")
        .startAt(Date.from(executeTime.atZone(ZoneId.systemDefault()).toInstant()))
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withMisfireHandlingInstructionFireNow()
        )
        .build();

    scheduler.scheduleJob(jobDetail, trigger);
  }

  @SneakyThrows
  public <T extends Job> boolean cancelScheduledJob(String jobName) {
    JobKey jobKey = JobKey.jobKey(jobName);
    return scheduler.deleteJob(jobKey);
  }

}
