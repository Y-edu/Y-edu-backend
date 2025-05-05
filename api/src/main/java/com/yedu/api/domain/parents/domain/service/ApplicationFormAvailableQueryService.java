package com.yedu.api.domain.parents.domain.service;

import com.yedu.api.domain.parents.domain.entity.ApplicationFormAvailable;
import com.yedu.api.domain.parents.domain.repository.ApplicationFormAvailableRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationFormAvailableQueryService {

  private final ApplicationFormAvailableRepository applicationFormAvailableRepository;

  public List<ApplicationFormAvailable> query(String applicationFormId) {
    return applicationFormAvailableRepository.findAllByApplicationFormId(applicationFormId);
  }
}
