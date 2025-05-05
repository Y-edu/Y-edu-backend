package com.yedu.api.domain.matching.domain.repository;

import com.yedu.api.domain.matching.domain.entity.MatchingTimetable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingTimetableRepository extends JpaRepository<MatchingTimetable, Long> {
  List<MatchingTimetable> findAllByClassMatching_ClassMatchingId(long classMatchingId);

  boolean existsByClassMatching_ClassMatchingId(long classMatchingId);
}
