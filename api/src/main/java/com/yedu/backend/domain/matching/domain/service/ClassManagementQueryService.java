package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleRetrieveRequest;
import com.yedu.backend.domain.matching.domain.entity.ClassManagement;
import com.yedu.backend.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.backend.domain.matching.domain.repository.ClassManagementRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClassManagementQueryService {

    private final ClassManagementRepository classManagementRepository;

    public Optional<ClassManagement> query(ClassScheduleRetrieveRequest request, Long id) {
        if (id != null){
            return classManagementRepository.findById(id);
        }
        if (request.classMatchingId() != null){
            return query(request.classMatchingId());
        }
        return Optional.empty();
    }

    public Optional<ClassManagement> query(Long classMatchingId) {
        return classManagementRepository.findByClassMatching_ClassMatchingId(classMatchingId);
    }

    public List<ClassManagement> query() {
        return classManagementRepository.findAllByRemindIsFalseAndCreatedAtIsLessThanEqualAndClassMatching_MatchStatus(LocalDateTime.now().minusDays(1L), MatchingStatus.매칭);
    }
}
