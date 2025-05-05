package com.yedu.api.admin.domain.service;

import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.repository.ClassMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSaveService {
  private final ClassMatchingRepository classMatchingRepository;

  public void saveClassMatching(ClassMatching classMatching) {
    classMatchingRepository.save(classMatching);
  }
}
