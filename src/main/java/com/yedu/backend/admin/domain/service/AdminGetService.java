package com.yedu.backend.admin.domain.service;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.parents.domain.repository.ApplicationFormRepository;
import com.yedu.backend.domain.parents.domain.repository.ClassMatchingRepository;
import com.yedu.backend.domain.parents.domain.repository.GoalRepository;
import com.yedu.backend.domain.parents.domain.repository.ParentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminGetService {
    private final ApplicationFormRepository applicationFormRepository;
    private final ClassMatchingRepository classMatchingRepository;
    private final GoalRepository goalRepository;
    private final ParentsRepository parentsRepository;

    public Parents parentsById(Long parentsId) {
        return parentsRepository.findById(parentsId)
                .orElseThrow();
    }

    public List<ApplicationForm> allApplication() {
        return applicationFormRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public List<ClassMatching> allMatching(ApplicationForm applicationForm) {
        return classMatchingRepository.findAllByApplicationForm(applicationForm);
    }

    public ApplicationForm applicationFormById(String applicationFormId) {
        return applicationFormRepository.findById(applicationFormId)
                .orElseThrow();
    }
}
