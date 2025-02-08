package com.yedu.backend.domain.parents.domain.service;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.parents.domain.repository.ApplicationFormRepository;
import com.yedu.backend.domain.parents.domain.repository.GoalRepository;
import com.yedu.backend.domain.parents.domain.repository.ParentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return applicationFormRepository.findById(applicationFormId)
                .orElseThrow();
    }

    public List<Goal> goalsByApplicationForm(ApplicationForm applicationForm) {
        return goalRepository.findAllByApplicationForm(applicationForm);
    }
}
