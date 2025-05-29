package com.yedu.api.domain.matching.application.usecase;

import static com.yedu.api.domain.matching.application.mapper.ClassMatchingMapper.mapToApplicationFormToTeacherResponse;
import static java.util.Comparator.comparing;

import com.yedu.api.domain.matching.application.dto.res.ApplicationFormResponse;
import com.yedu.api.domain.matching.application.dto.res.ClassMatchingForTeacherResponse;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.api.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.api.domain.matching.domain.service.ClassManagementQueryService;
import com.yedu.api.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.api.domain.matching.domain.service.ClassSessionQueryService;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.ApplicationFormAvailable;
import com.yedu.api.domain.parents.domain.entity.Goal;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.parents.domain.entity.constant.Online;
import com.yedu.api.domain.parents.domain.service.ApplicationFormAvailableQueryService;
import com.yedu.api.domain.parents.domain.service.ParentsGetService;
import com.yedu.api.domain.parents.domain.vo.DayTime;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherAvailable;
import com.yedu.api.domain.teacher.domain.entity.TeacherEnglish;
import com.yedu.api.domain.teacher.domain.entity.TeacherMath;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import com.yedu.api.domain.teacher.domain.repository.TeacherEnglishRepository;
import com.yedu.api.domain.teacher.domain.repository.TeacherMathRepository;
import com.yedu.api.domain.teacher.domain.service.TeacherGetService;
import com.yedu.cache.support.dto.TeacherNotifyApplicationFormDto;
import com.yedu.cache.support.storage.TeacherNotifyApplicationFormKeyStorage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassMatchingInfoUseCase {
  private final ClassMatchingGetService classMatchingGetService;
  private final ApplicationFormAvailableQueryService availableQueryService;
  private final ParentsGetService parentsGetService;
  private final TeacherNotifyApplicationFormKeyStorage teacherNotifyApplicationFormKeyStorage;
  private final TeacherGetService teacherGetService;
  private final ApplicationFormAvailableQueryService applicationFormAvailableQueryService;
  private final TeacherEnglishRepository teacherEnglishRepository;
  private final TeacherMathRepository teacherMathRepository;
  private final ClassManagementQueryService classManagementQueryService;
  private final ClassSessionQueryService classSessionQueryService;
  private final ClassMatchingRepository classMatchingRepository;

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

  public List<ApplicationFormResponse> applicationFormByMatchingId(List<Long> matchingIds, List<MatchingStatus> matchingStatus) {
    List<ClassMatching> matchings= getMatchings(matchingIds, matchingStatus);
    return matchings.stream().map(this::getApplicationFormResponses)
        .toList();
  }

  private ApplicationFormResponse getApplicationFormResponses(ClassMatching matching) {
    ApplicationForm applicationForm = matching.getApplicationForm();
    Teacher teacher = matching.getTeacher();

    return ApplicationFormResponse.builder()
        .applicationFormId(applicationForm.getApplicationFormId())
        .matchingId(matching.getClassMatchingId())
        .classCount(applicationForm.getClassCount())
        .district(applicationForm.getDistrict().toString())
        .dong(applicationForm.getDong())
        .pay(applicationForm.getPay())
        .matchingStatus(matching.getMatchStatus().toString())
        .childAge(applicationForm.getAge())
        .isOnline(applicationForm.getOnline().equals(Online.비대면))
        .source(applicationForm.getSource())
        .favoriteGender(applicationForm.getFavoriteGender().toString())
        .subject(applicationForm.getWantedSubject().toString())
        .favoriteStyle(applicationForm.getFavoriteStyle())
        .matchingRefuseReason(matching.getRefuseReason())
        .teacher(
            ApplicationFormResponse.Teacher.builder()
                .teacherId(teacher.getTeacherId())
                .realName(teacher.getTeacherInfo().getName())
                .nickName(teacher.getTeacherInfo().getNickName())
                .email(teacher.getTeacherInfo().getEmail())
                .phoneNumber(teacher.getTeacherInfo().getPhoneNumber())
                .university(teacher.getTeacherSchoolInfo().getUniversity())
                .major(teacher.getTeacherSchoolInfo().getMajor())
                .highSchool(teacher.getTeacherSchoolInfo().getHighSchool())
                .birth(teacher.getTeacherInfo().getBirth())
                .gender(teacher.getTeacherInfo().getGender().toString())
                .profileImage(teacher.getTeacherInfo().getProfile())
                .video(teacher.getTeacherInfo().getVideo())
                .introduce(teacher.getTeacherClassInfo().getIntroduce())
                .comment(teacher.getTeacherClassInfo().getComment())
                .teachingStyle(
                    List.of(
                        teacher.getTeacherClassInfo().getTeachingStyleInfo1(),
                        teacher.getTeacherClassInfo().getTeachingStyleInfo2()))
                .build())
        .build();
  }

  private List<ClassMatching> getMatchings(List<Long> matchingIds, List<MatchingStatus> statuses) {
    if (CollectionUtils.isEmpty(matchingIds)) {
      return classMatchingRepository.findByMatchStatusIn(statuses);
    }
    return classMatchingRepository.findByClassMatchingIdInAndMatchStatusIn(matchingIds, statuses);
  }

  public ApplicationFormResponse.Parents parents(ApplicationFormResponse applicationFormResponse) {
    Parents parents = parentsGetService.applicationFormByFormId(
        applicationFormResponse.getApplicationFormId())
        .getParents();

    return ApplicationFormResponse.Parents.builder()
            .parentId(parents.getParentsId())
            .kakaoName(parents.getKakaoName())
            .phoneNumber(parents.getPhoneNumber())
            .marketingAgree(parents.isMarketingAgree())
            .build();
  }

  public ApplicationFormResponse.TeacherEnglish english(ApplicationFormResponse.Teacher teacher) {
    Optional<TeacherEnglish> englishTeacher = teacherEnglishRepository.findByTeacher_TeacherId(teacher.getTeacherId());

    return ApplicationFormResponse.TeacherEnglish.builder()
            .possible(englishTeacher.isPresent())
            .experience(
                englishTeacher.map(TeacherEnglish::getTeachingExperience).orElse(null))
            .foreignExperience(
                englishTeacher.map(TeacherEnglish::getForeignExperience).orElse(null))
            .teachingStyle(
                englishTeacher.map(TeacherEnglish::getTeachingStyle).orElse(null))
            .teachingHistory(
                englishTeacher.map(TeacherEnglish::getTeachingHistory).orElse(null))
            .build();
  }

  public ApplicationFormResponse.TeacherMath math(ApplicationFormResponse.Teacher teacher) {
    Optional<TeacherMath> mathTeacher = teacherMathRepository.findByTeacher_TeacherId(teacher.getTeacherId());

    return ApplicationFormResponse.TeacherMath.builder()
        .possible(mathTeacher.isPresent())
        .experience(
            mathTeacher.map(TeacherMath::getTeachingExperience).orElse(null))
        .teachingStyle(mathTeacher.map(TeacherMath::getTeachingStyle).orElse(null))
        .teachingHistory(
            mathTeacher.map(TeacherMath::getTeachingHistory).orElse(null))
        .build();
  }


  public List<ApplicationFormResponse.AvailableTime> availableTimes(ApplicationFormResponse applicationFormResponse) {

    List<ApplicationFormAvailable> availables =
        applicationFormAvailableQueryService.query(applicationFormResponse.getApplicationFormId());

    return availables.stream()
            .map(
                it ->
                    ApplicationFormResponse.AvailableTime.builder()
                        .availableId(it.getId())
                        .day(it.getDay().toString())
                        .time(it.getAvailableTime().toString())
                        .build())
            .toList();
  }




  public ApplicationFormResponse.ClassManagement classManagement(ApplicationFormResponse applicationFormResponse) {
    Optional<ClassManagement> management = classManagementQueryService.queryWithSchedule(applicationFormResponse.getMatchingId());

    return ApplicationFormResponse.ClassManagement.builder()
            .classManagementId(
                management.map(ClassManagement::getClassManagementId).orElse(null))
            .textBook(management.map(ClassManagement::getTextbook).orElse(null))
            .firstDay(
                management
                    .map(ClassManagement::getFirstDay)
                    .map(LocalDate::toString)
                    .orElse(null))
            .schedule(
                management
                    .map(ClassManagement::getSchedules)
                    .map(
                        schedules ->
                            schedules.stream()
                                .map(
                                    it ->
                                        ApplicationFormResponse.Schedule.builder()
                                            .classScheduleId(it.getClassScheduleId())
                                            .day(it.getDay().toString())
                                            .start(it.getClassTime().getStart().toString())
                                            .classMinute(it.getClassTime().getClassMinute())
                                            .build())
                                .toList())
                    .orElse(null))
            .sessions(
                management
                    .map(classSessionQueryService::query)
                    .map(
                        sessions ->
                            sessions.stream()
                                .map(
                                    it ->
                                        ApplicationFormResponse.Session.builder()
                                            .classSessionId(it.getClassSessionId())
                                            .date(it.getSessionDate().toString())
                                            .start(it.getClassTime().getStart().toString())
                                            .classMinute(it.getClassTime().getClassMinute())
                                            .understanding(it.getUnderstanding())
                                            .homeworkPercentage(it.getHomeworkPercentage())
                                            .cancel(it.isCancel())
                                            .cancelReason(it.getCancelReason())
                                            .completed(it.isCompleted())
                                            .build())
                                .toList())
                    .orElse(null))
            .build();
  }
}
