package com.yedu.api.domain.parents.domain.job;

import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormChangeRequest;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormRequest;
import com.yedu.api.domain.parents.application.usecase.ParentsManageUseCase;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Goal;
import com.yedu.api.domain.parents.domain.service.ParentsGetService;
import com.yedu.api.domain.teacher.domain.entity.constant.District;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class TeacherChangeJob implements Job {

  private final ClassMatchingRepository matchingRepository;
  private final ParentsGetService parentsGetService;
  private final ParentsManageUseCase parentsManageUseCase;

  @Override
  public void execute(JobExecutionContext context) {
    JobDataMap jobDataMap = context.getMergedJobDataMap();
    executeNow(jobDataMap);
  }

  public void executeNow(JobDataMap jobDataMap) {
    long matchingId = jobDataMap.getLong("id");
    ApplicationFormChangeRequest changeRequest = (ApplicationFormChangeRequest) jobDataMap.get("request");

    ClassMatching matching = matchingRepository.findById(matchingId).orElseThrow();
    ApplicationForm previousApplicationForm = matching.getApplicationForm();
    District previousDistrict = previousApplicationForm.getDistrict();
    long previousTeacherId = matching.getTeacher().getTeacherId();

    if (previousDistrict.equals(changeRequest.district())) {
      log.info(">>> 지역 동일하여 기존 과외 재공지 :{}" , changeRequest.district());

      parentsManageUseCase.sendAlarmTalk(previousApplicationForm, new ArrayList<>(), previousTeacherId);
      return;
    }

    log.info(">>> 지역 변경으로 인한 신규 과외 생성 :{}" , changeRequest.district());
    List<String> classGoals = parentsGetService.goalsByApplicationForm(previousApplicationForm)
        .stream().map(Goal::getClassGoal)
        .toList();

    parentsManageUseCase.saveParentsAndApplication(
        ApplicationFormRequest.builder()
            .phoneNumber(changeRequest.phoneNumber())
            .age(previousApplicationForm.getAge())
            .wantedSubject(previousApplicationForm.getWantedSubject())
            .wantedTime(changeRequest.wantedTime())
            .classGoals(classGoals)
            .favoriteGender(changeRequest.favoriteGender())
            .favoriteStyle(changeRequest.favoriteStyle())
            .online(previousApplicationForm.getOnline())
            .classCount(changeRequest.useSameClassCount() ? previousApplicationForm.getClassCount() : changeRequest.classCount())
            .classTime(changeRequest.useSameClassCount() ? previousApplicationForm.getClassTime() : changeRequest.classTime())
            .district(changeRequest.district())
            .dong(changeRequest.dong())
            .source(previousApplicationForm.getSource())
            .dayTimes(Collections.emptyList())
            .build(),
        false, previousTeacherId);
  }
}
