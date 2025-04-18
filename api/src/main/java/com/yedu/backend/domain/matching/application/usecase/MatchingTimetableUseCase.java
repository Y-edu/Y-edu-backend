package com.yedu.backend.domain.matching.application.usecase;

import com.yedu.backend.domain.matching.application.dto.req.MatchingTimeTableRetrieveRequest;
import com.yedu.backend.domain.matching.application.dto.res.MatchingTimetableRetrieveResponse;
import com.yedu.backend.domain.matching.domain.entity.MatchingTimetable;
import com.yedu.backend.domain.matching.domain.service.MatchingTimetableQueryService;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherAvailable;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchingTimetableUseCase {
    private final MatchingTimetableQueryService matchingTimetableQueryService;

    public MatchingTimetableRetrieveResponse retrieveMatchingTimetable(MatchingTimeTableRetrieveRequest request) {
        List<MatchingTimetable> timetables = matchingTimetableQueryService.query(request.classMatchingId());
        SortedMap<Day, List<LocalTime>> sortedTimetable = getDayListSortedMap(timetables);
        return new MatchingTimetableRetrieveResponse(sortedTimetable);
    }

    private SortedMap<Day, List<LocalTime>> getDayListSortedMap(List<MatchingTimetable> timetables) {
        SortedMap<Day, List<LocalTime>> availableTimes = new TreeMap<>(Comparator.comparingInt(Day::getDayNum));
        Arrays.stream(Day.values()).forEach(day -> availableTimes.put(day, new ArrayList<>()));

        timetables.forEach(timetable -> {
            Day day = timetable.getDay();
            List<LocalTime> availables = availableTimes.get(day);
            availables.add(timetable.getTimetableTime());
        });
        return availableTimes;
    }
}
