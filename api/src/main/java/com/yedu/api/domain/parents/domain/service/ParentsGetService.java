package com.yedu.api.domain.parents.domain.service;

import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Goal;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.parents.domain.repository.ApplicationFormRepository;
import com.yedu.api.domain.parents.domain.repository.GoalRepository;
import com.yedu.api.domain.parents.domain.repository.ParentsRepository;
import com.yedu.api.global.exception.parents.ApplicationFormNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentsGetService {
  private final ParentsRepository parentsRepository;
  private final ApplicationFormRepository applicationFormRepository;
  private final GoalRepository goalRepository;

  public Optional<Parents> optionalParentsByPhoneNumber(String phoneNumber) {
    return parentsRepository.findByPhoneNumber(phoneNumber);
  }

  public ApplicationForm applicationFormByFormId(String applicationFormId) {
    return applicationFormRepository
        .findById(applicationFormId)
        .orElseThrow(() -> new ApplicationFormNotFoundException(applicationFormId));
  }

  public List<Goal> goalsByApplicationForm(ApplicationForm applicationForm) {
    return goalRepository.findAllByApplicationForm(applicationForm);
  }
}
