package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.entity.MatchingTimetable;
import com.yedu.backend.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.backend.domain.matching.domain.repository.MatchingTimetableRepository;
import com.yedu.backend.domain.parents.domain.vo.DayTime;
import com.yedu.backend.global.exception.matching.MatchingNotFoundException;
import com.yedu.backend.global.exception.matching.MatchingTimetableAlreadyException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingTimetableCommandService {
  private final MatchingTimetableRepository matchingTimetableRepository;
  private final ClassMatchingRepository classMatchingRepository;

  public void matchingTimetable(long classMatchingId, List<DayTime> dayTimes) {
    if (matchingTimetableRepository.existsByClassMatching_ClassMatchingId(classMatchingId))
      throw new MatchingTimetableAlreadyException(classMatchingId);
    ClassMatching classMatching =
        classMatchingRepository
            .findById(classMatchingId)
            .orElseThrow(() -> new MatchingNotFoundException(classMatchingId));
    classMatching.makeMatchingTimetable();

    dayTimes.forEach(
        dayTime ->
            dayTime
                .getTimes()
                .forEach(
                    time ->
                        matchingTimetableRepository.save(
                            MatchingTimetable.builder()
                                .classMatching(classMatching)
                                .day(dayTime.getDay())
                                .timetableTime(time)
                                .build())));
  }
}
