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

@Service
@RequiredArgsConstructor
public class ParentsSaveService {
    private final ParentsRepository parentsRepository;
    private final ApplicationFormRepository applicationFormRepository;
    private final GoalRepository goalRepository;

    public Parents saveParents(Parents parents) {
        return parentsRepository.save(parents);
    }

    public void saveApplication(ApplicationForm applicationForm, List<Goal> goals) {
        applicationFormRepository.save(applicationForm);
        goals.forEach(goalRepository::save);
    }
}
