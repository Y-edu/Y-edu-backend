package com.yedu.backend.domain.matching.application.usecase;

import com.yedu.backend.domain.matching.application.mapper.ClassMatchingMapper;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingSaveService;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassMatchingManageUseCase {
    private final ClassMatchingSaveService classMatchingSaveService;

    public void saveAllClassMatching(List<Teacher> teachers, ApplicationForm applicationForm) {
        teachers.stream()
                .map(teacher -> ClassMatchingMapper.mapToClassMatching(teacher, applicationForm))
                .toList()
                .forEach(classMatchingSaveService::save);
    }
}
