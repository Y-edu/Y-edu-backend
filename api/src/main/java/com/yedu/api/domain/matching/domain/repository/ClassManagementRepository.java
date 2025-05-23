package com.yedu.api.domain.matching.domain.repository;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassManagementRepository extends JpaRepository<ClassManagement, Long> {

  Optional<ClassManagement> findByClassMatching_ClassMatchingId(Long classMatchingId);

  List<ClassManagement>
      findAllByRemindIsFalseAndCreatedAtIsLessThanEqualAndClassMatching_MatchStatus(
          LocalDateTime time, MatchingStatus status);

  @Query(
      "SELECT cm FROM ClassManagement cm JOIN FETCH cm.schedules WHERE cm.classMatching.classMatchingId = :classMatchingId")
  Optional<ClassManagement> findWithSchedule(@Param("classMatchingId") Long classMatchingId);
}
