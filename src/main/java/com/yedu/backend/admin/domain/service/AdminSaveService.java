package com.yedu.backend.admin.domain.service;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.repository.ClassMatchingRepository;
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
