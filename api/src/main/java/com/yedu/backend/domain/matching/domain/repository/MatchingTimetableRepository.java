package com.yedu.backend.domain.matching.domain.repository;

import com.yedu.backend.domain.matching.domain.entity.MatchingTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchingTimetableRepository extends JpaRepository<MatchingTimetable, Long> {
    List<MatchingTimetable> findAllByClassMatching_ClassMatchingId(long classMatchingId);
}
