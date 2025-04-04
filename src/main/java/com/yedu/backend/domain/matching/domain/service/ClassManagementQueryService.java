package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.domain.matching.domain.entity.ClassManagement;
import com.yedu.backend.domain.matching.domain.repository.ClassManagementRepository;
import com.yedu.backend.global.exception.matching.ClassManagementNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClassManagementQueryService {
    private final ClassManagementRepository classManagementRepository;

    public ClassManagement queryById(Long id){
        return classManagementRepository.findById(id)
            .orElseThrow(()-> new ClassManagementNotFoundException("일치하는 매칭 관리 정보를 찾을 수 없습니다"));
    }
}
