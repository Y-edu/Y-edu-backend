package com.yedu.api.domain.matching.domain.service;

import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.MatchingTimetable;
import com.yedu.api.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.api.domain.matching.domain.repository.MatchingTimetableRepository;
import com.yedu.api.domain.parents.domain.vo.DayTime;
import com.yedu.api.global.exception.matching.MatchingNotFoundException;
import com.yedu.api.global.exception.matching.MatchingTimetableAlreadyException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingTimetableCommandService {
  private final MatchingTimetableRepository matchingTimetableRepository;
  private final ClassMatchingRepository classMatchingRepository;

  public ClassMatching matchingTimetable(long classMatchingId, List<DayTime> dayTimes) {
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

    Hibernate.initialize(classMatching);
    return classMatching;
  }
}
