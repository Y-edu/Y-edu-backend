package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.domain.matching.domain.entity.MatchingTimetable;
import com.yedu.backend.domain.matching.domain.repository.MatchingTimetableRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingTimetableQueryService {
  private final MatchingTimetableRepository matchingTimetableRepository;

  public List<MatchingTimetable> query(long classMatchingId) {
    return matchingTimetableRepository.findAllByClassMatching_ClassMatchingId(classMatchingId);
  }
}
