package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.repository.ClassMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassMatchingGetService {
    private final ClassMatchingRepository classMatchingRepository;

    public ClassMatching classMatchingByApplicationFormIdAndTeacherId(String applicationFormId, long teacherId) {
        return classMatchingRepository.findByApplicationForm_ApplicationFormIdAndTeacher_TeacherId(applicationFormId, teacherId)
                .orElseThrow();
    }
}
