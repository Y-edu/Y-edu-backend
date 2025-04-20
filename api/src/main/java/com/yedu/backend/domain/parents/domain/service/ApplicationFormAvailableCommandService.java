package com.yedu.backend.domain.parents.domain.service;

import com.yedu.backend.domain.parents.domain.entity.ApplicationFormAvailable;
import com.yedu.backend.domain.parents.domain.repository.ApplicationFormAvailableRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class ApplicationFormAvailableCommandService {

  private final ApplicationFormAvailableRepository applicationFormAvailableRepository;

  public void saveAll(List<ApplicationFormAvailable> applicationFormAvailables) {
    applicationFormAvailableRepository.saveAll(applicationFormAvailables);
  }
}
