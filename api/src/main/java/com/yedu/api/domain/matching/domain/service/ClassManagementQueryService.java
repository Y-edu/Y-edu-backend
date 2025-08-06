package com.yedu.api.domain.matching.domain.service;

import com.yedu.api.domain.matching.application.dto.req.ClassScheduleRetrieveRequest;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.api.domain.matching.domain.repository.ClassManagementRepository;
import com.yedu.payment.PaymentOperationWrapper;
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

  private final PaymentOperationWrapper paymentOperation;

  public Optional<ClassManagement> query(ClassScheduleRetrieveRequest request, Long id) {
    if (id != null) {
      return classManagementRepository.findById(id);
    }
    if (request.classMatchingId() != null) {
      return query(request.classMatchingId());
    }
    return Optional.empty();
  }

  public Optional<ClassManagement> query(Long classMatchingId) {
    return classManagementRepository.findByClassMatching_ClassMatchingId(classMatchingId);
  }

  public Optional<ClassManagement> queryWithSchedule(Long classMatchingId) {
    return classManagementRepository.findWithSchedule(classMatchingId);
  }

  public List<ClassManagement> query() {
    return classManagementRepository
        .findAllByRemindIsFalseAndCreatedAtIsLessThanEqualAndClassMatching_MatchStatus(
            LocalDateTime.now().minusDays(1L), MatchingStatus.매칭);
  }

  public List<ClassManagement> query(List<ClassMatching> matchings) {
     return classManagementRepository.findAllByClassMatchingIn(matchings);
  }
}
