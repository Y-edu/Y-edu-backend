package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.domain.matching.domain.entity.MatchingTimetable;
import com.yedu.backend.domain.matching.domain.repository.MatchingTimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingTimetableQueryService {
    private final MatchingTimetableRepository matchingTimetableRepository;

    public List<MatchingTimetable> query(long classMatchingId) {
        return matchingTimetableRepository.findAllByClassMatching_ClassMatchingId(classMatchingId);
    }
}
