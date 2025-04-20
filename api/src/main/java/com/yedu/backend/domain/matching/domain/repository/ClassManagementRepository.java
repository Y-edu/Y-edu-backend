package com.yedu.backend.domain.matching.domain.repository;

import com.yedu.backend.domain.matching.domain.entity.ClassManagement;
import com.yedu.backend.domain.matching.domain.entity.constant.MatchingStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassManagementRepository extends JpaRepository<ClassManagement, Long> {

  Optional<ClassManagement> findByClassMatching_ClassMatchingId(Long classMatchingId);

  List<ClassManagement>
      findAllByRemindIsFalseAndCreatedAtIsLessThanEqualAndClassMatching_MatchStatus(
          LocalDateTime time, MatchingStatus status);
}
