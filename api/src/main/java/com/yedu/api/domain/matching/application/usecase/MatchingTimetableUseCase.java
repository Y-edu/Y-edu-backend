package com.yedu.api.domain.matching.application.usecase;

import com.yedu.api.domain.matching.application.dto.req.MatchingTimeTableRequest;
import com.yedu.api.domain.matching.application.dto.req.MatchingTimeTableRetrieveByTokenRequest;
import com.yedu.api.domain.matching.application.dto.req.MatchingTimeTableRetrieveRequest;
import com.yedu.api.domain.matching.application.dto.res.MatchingTimetableRetrieveResponse;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.MatchingTimetable;
import com.yedu.api.domain.matching.domain.service.MatchingTimetableCommandService;
import com.yedu.api.domain.matching.domain.service.MatchingTimetableQueryService;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import com.yedu.api.global.event.publisher.EventPublisher;
import com.yedu.cache.support.dto.MatchingTimeTableDto;
import com.yedu.cache.support.storage.KeyStorage;
import com.yedu.common.event.bizppurio.PayNotificationEvent;
import java.time.LocalTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingTimetableUseCase {
  private final MatchingTimetableQueryService matchingTimetableQueryService;
  private final MatchingTimetableCommandService matchingTimetableCommandService;
  private final KeyStorage<MatchingTimeTableDto> matchingTimetableKeyStorage;
  private final EventPublisher eventPublisher;

  public MatchingTimetableRetrieveResponse retrieveMatchingTimetable(
      MatchingTimeTableRetrieveRequest request) {
    List<MatchingTimetable> timetables =
        matchingTimetableQueryService.query(request.classMatchingId());
    SortedMap<Day, List<LocalTime>> sortedTimetable = getDayListSortedMap(timetables);
    return new MatchingTimetableRetrieveResponse(sortedTimetable);
  }

  public MatchingTimetableRetrieveResponse retrieveMatchingTimetable(
      MatchingTimeTableRetrieveByTokenRequest request) {
    MatchingTimeTableDto matchingTimeTableDto =
        matchingTimetableKeyStorage.get(request.classMatchingToken());
    List<MatchingTimetable> timetables =
        matchingTimetableQueryService.query(matchingTimeTableDto.matchingId());
    SortedMap<Day, List<LocalTime>> sortedTimetable = getDayListSortedMap(timetables);
    return new MatchingTimetableRetrieveResponse(sortedTimetable);
  }

  private SortedMap<Day, List<LocalTime>> getDayListSortedMap(List<MatchingTimetable> timetables) {
    SortedMap<Day, List<LocalTime>> availableTimes =
        new TreeMap<>(Comparator.comparingInt(Day::getDayNum));
    Arrays.stream(Day.values()).forEach(day -> availableTimes.put(day, new ArrayList<>()));

    timetables.forEach(
        timetable -> {
          Day day = timetable.getDay();
          List<LocalTime> availables = availableTimes.get(day);
          availables.add(timetable.getTimetableTime());
        });
    return availableTimes;
  }

  public void matchingTimetable(MatchingTimeTableRequest request) {
    MatchingTimeTableDto matchingTimeTableDto =
        matchingTimetableKeyStorage.get(request.classMatchingToken());
    ClassMatching classMatching =
        matchingTimetableCommandService.matchingTimetable(
            matchingTimeTableDto.matchingId(), request.dayTimes());

    ApplicationForm applicationForm = classMatching.getApplicationForm();
    PayNotificationEvent event =
        new PayNotificationEvent(
            applicationForm.getParents().getPhoneNumber(),
            classMatching.getTeacher().getTeacherInfo().getNickName(),
            applicationForm.getPay());

    eventPublisher.publishPayNotificationEvent(event);
  }
}
