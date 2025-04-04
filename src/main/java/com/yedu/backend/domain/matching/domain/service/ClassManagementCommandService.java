package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.domain.matching.domain.entity.ClassManagement;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.repository.ClassManagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ClassManagementCommandService {
    private final ClassManagementRepository classManagementRepository;

    public ClassManagement save(ClassMatching classMatching){
        ClassManagement classManagement = ClassManagement.builder()
            .classMatching(classMatching)
            .build();

        return classManagementRepository.save(classManagement);
    }

    public void delete(ClassManagement classManagement) {
        classManagementRepository.delete(classManagement);
    }
}
