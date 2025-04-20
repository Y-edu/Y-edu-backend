package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.repository.ClassMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassMatchingSaveService {
  private final ClassMatchingRepository classMatchingRepository;

  public void save(ClassMatching classMatching) {
    classMatchingRepository.save(classMatching);
  }
}
