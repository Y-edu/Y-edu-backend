package com.yedu.backend.domain.matching.application.usecase;

import static com.yedu.backend.domain.matching.application.mapper.ClassMatchingMapper.mapToApplicationFormToTeacherResponse;
import static java.util.Comparator.comparing;

import com.yedu.backend.domain.matching.application.dto.res.ClassMatchingForTeacherResponse;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.ApplicationFormAvailable;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.parents.domain.service.ApplicationFormAvailableQueryService;
import com.yedu.backend.domain.parents.domain.service.ParentsGetService;
import com.yedu.backend.domain.parents.domain.vo.DayTime;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherAvailable;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import com.yedu.backend.domain.teacher.domain.service.TeacherGetService;
import com.yedu.cache.support.dto.TeacherNotifyApplicationFormDto;
import com.yedu.cache.support.storage.TeacherNotifyApplicationFormKeyStorage;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassMatchingInfoUseCase {
  private final ClassMatchingGetService classMatchingGetService;
  private final ApplicationFormAvailableQueryService availableQueryService;
  private final ParentsGetService parentsGetService;
  private final TeacherNotifyApplicationFormKeyStorage teacherNotifyApplicationFormKeyStorage;
  private final TeacherGetService teacherGetService;

  public ClassMatchingForTeacherResponse applicationFormToTeacher(String token) {
    TeacherNotifyApplicationFormDto dto = teacherNotifyApplicationFormKeyStorage.get(token);
    ClassMatching classMatching = classMatchingGetService.getById(dto.matchingId());
    ApplicationForm applicationForm = classMatching.getApplicationForm();
    Teacher teacher = classMatching.getTeacher();

    List<DayTime> parentDayTimes =
        getDayTimesFromAvailable(
            availableQueryService.query(dto.applicationFormId()),
            ApplicationFormAvailable::getDay,
            ApplicationFormAvailable::getAvailableTime);

    List<String> goals =
        parentsGetService.goalsByApplicationForm(applicationForm).stream()
            .map(Goal::getClassGoal)
            .toList();

    List<DayTime> teacherDayTimes =
        getDayTimesFromAvailable(
            teacherGetService.availablesByTeacher(teacher),
            TeacherAvailable::getDay,
            TeacherAvailable::getAvailableTime);

    return mapToApplicationFormToTeacherResponse(
        classMatching, applicationForm, goals, parentDayTimes, teacherDayTimes);
  }

  private <T> List<DayTime> getDayTimesFromAvailable(
      List<T> availableList, Function<T, Day> dayExtractor, Function<T, LocalTime> timeExtractor) {
    return new ArrayList<>(
        availableList.stream()
            .collect(
                Collectors.groupingBy(
                    dayExtractor, Collectors.mapping(timeExtractor, Collectors.toList())))
            .entrySet()
            .stream()
            .map(entry -> new DayTime(entry.getKey(), entry.getValue()))
            .sorted(comparing(dayTime -> dayTime.getDay().getDayNum()))
            .toList());
  }
}
