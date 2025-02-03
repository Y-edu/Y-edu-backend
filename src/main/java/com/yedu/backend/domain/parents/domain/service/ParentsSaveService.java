package com.yedu.backend.domain.parents.domain.service;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.parents.domain.repository.ApplicationFormRepository;
import com.yedu.backend.domain.parents.domain.repository.ParentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentsSaveService {
    private final ParentsRepository parentsRepository;
    private final ApplicationFormRepository applicationFormRepository;

    public Parents saveParents(Parents parents) {
        return parentsRepository.save(parents);
    }

    public void saveApplication(ApplicationForm applicationForm) {
        applicationFormRepository.save(applicationForm);
    }
}
