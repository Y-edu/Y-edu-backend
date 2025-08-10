package com.yedu.api.domain.parents.domain.job;

import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.api.domain.matching.domain.repository.ClassMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class MatchingStatusPauseJob implements Job {

  private final ClassMatchingRepository matchingRepository;

  @Override
  public void execute(JobExecutionContext context) {
    Long id = context.getMergedJobDataMap().getLong("id");

    matchingRepository.findById(id).ifPresent(matching -> matching.update(MatchingStatus.일시중단));
  }
}
